package org.jeecg.modules.quartz.job;

import lombok.Data;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.yy.api.GooglePlacesDetails;
import org.jeecg.modules.yy.api.GooglePlacesNearbySearch;
import org.jeecg.modules.yy.place.entity.*;
import org.jeecg.modules.yy.place.service.*;
import org.jeecg.modules.yy.place.vo.Location;
import org.jeecg.modules.yy.utils.GeoGridGenerator;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 利用Google Map API取得地點情報的定时任务
 *
 * ■設計思想：
 *  【地点采集中心坐标】和【标签情报】中標記為【對象】的記錄做笛卡爾積，對每一個組合
 *  通過調用Google Map API取得附近的地點情報，然後將該地點情報追加進【地点情报】和【地点标签关系】表。
 *  注意點：
 *  1.在向表追加地點情報前，需要通過Google的地點ID（Place_ID）進行重複確認，重複的不需要追加
 *  2.同一中心點附近的地點情報如果超過20條，需要進行下一頁請求。請求3頁（60條）后如果還有，中止處理。
 *    將該中心點的信息登入【記錄表】標記爲【未完成】，該中心點的【對象】字段不更新。
 *  3.每一個組合對應的地點情報取得完全的情況下，該中心點的信息登入【記錄表】標記爲【已完成】，該中心點的【對象】字段更新為【非對象】
 *
 * ■關聯DB表：
 *  ・地点采集中心坐标(yy_place_search_center_coord)　⇒　RU
 *  ・标签情报(yy_place_tag)　⇒　RU
 *  ・地点情报(yy_place)　⇒　C
 *  ・地点标签关系(yy_place_tag_rel)　⇒　C
 *  ・地点采集记录(yy_place_search_record)　⇒　C
 *
 * ■Google Map API：
 *  ・附近搜索（Search Nearby）
 *  ・地點詳細（Place Details）
 *  ・地點圖片（Place Photos）
 *
 * ■詳細
 *  1.從設定文件中取得Google Map API的密鑰鍵 （將密鑰鍵保存在外部是爲了以後變更方便）
 *  2.從【地点采集中心坐标】表中取得標記為【對象】的記錄，生成坐標一覽。
 *    2.1 利用該記錄的【網格維數】【采集半徑】的值，計算出指定網格的各個點的坐標。
 *    2.2 將中心坐標和計算出的對應網格坐標，追加進坐標一覽
 *        如：  "中心坐標1" :  "網格坐標1"
 *                           "網格坐標2"
 *                           "網格坐標3"
 *             "中心坐標2" :  "網格坐標1"
 *                           "網格坐標2"
 *  3.從【标签情报】表中取得標記為【對象】的記錄，生成地點標簽一覽。
 *  4.以2，3的結果列表做笛卡爾積的遍歷處理：
 *    4.1 2處理中生成的坐標一覽作爲外循環，3處理中取得的地點標簽一覽作爲内循環。
 *    4.2 每一次遍歷裏做以下的API調用，數據登入處理
 *        4.2.0 保持當前外循環的中心坐標和當前内循環的地點標簽
 *        4.2.1 調用Google Map API的【附近搜索】處理，將取得的地點做成一個臨時的地點列表
 *              4.2.1.1 取得結果 < 20條，將所有結果存入臨時地點列表
 *              4.2.1.2 取得結果 == 20條，查看是否存在下一頁，將所有結果存入臨時地點列表
 *              4.2.1.3 重複以上的1，2步驟，如果第三頁的結果 == 20條 并且 存在下一頁
 *                      4.2.1.3.1 中止處理，將當前中心坐標和當前的地點標簽的地點取得結果登入到【地点采集记录】表，結果更新為【未完成】
 *        4.2.2 遍歷前一步做成的臨時地點列表，取得地點ID進行確認，如果不存在，執行以下處理
 *              4.2.2.1 使用地點ID調用Google Map API的【地點詳細】處理，將取得的基本情報保持
 *              4.2.2.2 使用地點ID調用Google Map API的【地點圖片】處理，將取得的圖片情報保持
 *              4.2.2.3 將取得的當前地點的所有情報登入到【地点情报】表
 *              4.2.2.4 將當前地點和當前的地點標簽的關係登入到【地点标签关系】表
 *        4.2.3 將當前中心坐標和當前的地點標簽的地點取得結果登入到【地点采集记录】表，結果更新為【正常】
 *  5 本定時任務的處理中，任何位置發生例外時，將當前中心坐標和當前的地點標簽的地點取得結果登入到【地点采集记录】表，結果更新為【異常】
 *
 * @Author 姜
 */
@Slf4j
@Service
public class GetGooglePlaceInfoJob implements Job {

	@Autowired
	private IYyPlaceService yyPlaceService;
	@Autowired
	private IYyPlaceTagRelService yyPlaceTagRelService;
	@Autowired
	private IYyPlaceSearchCenterCoordService yyPlaceSearchCenterCoordService;
	@Autowired
	private IYyPlaceSearchRecordService yyPlaceSearchRecordService;
	@Autowired
	private IYyPlaceTagService yyPlaceTagService;
	@Autowired
	private GeoGridGenerator geoGridGenerator;
	@Autowired
	private GooglePlacesNearbySearch placesNearbySearch;
	@Autowired
	private GooglePlacesDetails placesDetails;

	// 地点标签
	private YyPlaceTag yyPlaceTag;
	// 中心坐标
	private YyPlaceSearchCenterCoord centerCoord;

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		try {
			log.info(" Job Execution key：" + jobExecutionContext.getJobDetail().getKey());
			log.info(String.format(" 使用Google Map API 获取地点信息的任务 開始!  时间:" + DateUtils.getTimestamp()));
			// 0 定義
			List<YyPlaceSearchCenterCoord> centerCoords = new ArrayList<>();
			List<String> primaryTypeList = new ArrayList<>();
			List<YyPlaceTagRel> placeTagRelList = new ArrayList<>();
			List<YyPlace> tmpPlaceList = new ArrayList<>();
			YyPlaceTagRel yyPlaceTagRel = new YyPlaceTagRel();
			Double centerLat=0.0;
			Double centerLng=0.0;
			Double userRating=0.0;
			int userNums=0;
			String grids="";
			String radius="";

			String getPlaceItems = "places.id,places.rating";  // 追加取得項目時用逗號分隔
			String getPlaceItemsForDetail = "id,rating,userRatingCount,displayName,types,primaryType,formattedAddress,location,internationalPhoneNumber,websiteUri,reservable,rating,editorialSummary";  //photos

			// 1.從設定文件中取得Google Map API的密鑰鍵  -- 定義在yaml文件中，由api類取得
			// 2.從【地点采集中心坐标】表中取得標記為【對象】的記錄，生成坐標一覽。
			centerCoords = makeLocationList();

			// 3.從【标签情报】表中取得標記為【對象】的記錄，生成地點標簽一覽。
			primaryTypeList = yyPlaceTagService.getPrimaryTypeByFlg();

			// 4.以2，3的結果列表做笛卡爾積的遍歷處理
			// 4.1 2處理中生成的坐標一覽作爲外循環，3處理中取得的地點標簽一覽作爲内循環。
			YyPlaceSearchCenterCoord coord;
			YyPlaceSearchRecord searchRecord;
			int placeNums = 0;
			for (int i = 0 ; i < centerCoords.size() ; i++){
				coord = centerCoords.get(i);
				// 4.2 每一次遍歷裏做以下的API調用，數據登入處理
				if ("1".equals(coord.getPlaceSearchFlg())){
					// 4.2.0 保持當前外循環的中心坐標和當前内循環的地點標簽
					centerLat = coord.getLatitude();
					centerLng = coord.getLongitude();
					grids = coord.getGridNums();
					radius = coord.getRadius();
					userRating = coord.getUserRating();
					userNums = coord.getUserNums();
					continue;
				}

				for (String primaryType:primaryTypeList) {
					// 4.2.1 調用Google Map API的【附近搜索】處理，將取得的地點做成一個臨時的地點列表
					tmpPlaceList = placesNearbySearch.searchNearby(
							coord.getLatitude(),
							coord.getLongitude(),
							radius,
							userRating,
							primaryType,
							getPlaceItems);

					// 4.2.2 遍歷前一步做成的臨時地點列表，取得地點ID進行確認，如果不存在，執行以下處理
					for (YyPlace place : tmpPlaceList) {
						if (IsPlaceExist(place.getGooglePladeId())) {
							continue;
						}
						// 4.2.2.1 使用地點ID調用Google Map API的【地點詳細】處理，將取得的基本情報保持
						// 4.2.2.2 使用地點ID調用Google Map API的【地點圖片】處理，將取得的圖片情報保持
						placeTagRelList = placesDetails.getPlaceDetails(place, userNums, getPlaceItemsForDetail);

						// 地點評價用戶數 < 10 不作爲采集對象
						if (placeTagRelList.size() == 0 && place.getGooglePrimaryType() == null) {
							continue;
						}

						// 4.2.2.3 將取得的當前地點的所有情報登入到【地点情报】表
						// 4.2.2.4 將當前地點和當前的地點標簽的關係登入到【地点标签关系】表
						yyPlaceService.saveMain(place, placeTagRelList);
						placeNums++;
					}

					// 4.2.3 將當前中心坐標和當前的地點標簽的地點取得結果登入到【地点采集记录】表，結果更新為【正常】
					if (i == centerCoords.size() -1 || "1".equals(centerCoords.get(i+1).getPlaceSearchFlg())){
						searchRecord = new YyPlaceSearchRecord();
						searchRecord.setLatitude(centerLat);
						searchRecord.setLongitude(centerLng);
						searchRecord.setPrimaryType(primaryType);
						searchRecord.setGridNums(grids);
						searchRecord.setRadius(radius);
						searchRecord.setUserRating(userRating);
						searchRecord.setUserNums(userNums);
						searchRecord.setPlaceSearchRst("0");
						yyPlaceSearchRecordService.save(searchRecord);
					}
				}
			}

			log.info(String.format(" 使用Google Map API 获取地点信息的任务 終了!  采集地點:" + placeNums));
			log.info(String.format(" 使用Google Map API 获取地点信息的任务 終了!  时间:" + DateUtils.getTimestamp()));
		} catch (Exception e) {
			// 5 本定時任務的處理中，任何位置發生例外時，將當前中心坐標和當前的地點標簽的地點取得結果登入到【地点采集记录】表，結果更新為【異常】
			log.info(String.format(" 使用Google Map API 获取地点信息的任务 異常!  时间:" + DateUtils.getTimestamp()));
			log.info(String.format(" 使用Google Map API 获取地点信息的任务 異常!  内容:" + e.getMessage()));
		}
	}

	/**
	 * 2.從【地点采集中心坐标】表中取得標記為【對象】的記錄，生成坐標一覽。
	 *    2.1 利用該記錄的【網格維數】【采集半徑】的值，計算出指定網格的各個點的坐標。
	 *    2.2 將中心坐標和計算出的對應網格坐標，追加進坐標一覽
	 *        如：  "中心坐標1" :  "網格坐標1"
	 *                           "網格坐標2"
	 *                           "網格坐標3"
	 *             "中心坐標2" :  "網格坐標1"
	 *                           "網格坐標2"
	 */
	private List<YyPlaceSearchCenterCoord> makeLocationList(){
		List<double[]> gridPoints = new ArrayList<>();
		List<YyPlaceSearchCenterCoord> resList = new ArrayList<>();
		List<YyPlaceSearchCenterCoord> centerCoords = yyPlaceSearchCenterCoordService.getSearchCentersByFlg();

		if (centerCoords != null ){
			YyPlaceSearchCenterCoord resItem;

			for(YyPlaceSearchCenterCoord centerCoord:centerCoords){
				// 中心坐标設定
				resList.add(centerCoord);

				// 網格坐標設定
				gridPoints = geoGridGenerator.generateGrid(
						centerCoord.getLatitude(),
						centerCoord.getLongitude(),
						Integer.parseInt(centerCoord.getGridNums()),
						Integer.parseInt(centerCoord.getRadius()));

				for (double[] point : gridPoints) {
					resItem = new YyPlaceSearchCenterCoord();
					resItem.setLatitude(point[0]);
					resItem.setLongitude(point[1]);
					resList.add(resItem);
				}
			}
		}
		return resList;
	}

	/**
	 * 用placeId判斷該地點情報是否已經存在
	 */
	private boolean IsPlaceExist(String id){
		return yyPlaceService.getCountByPlaceId(id) > 0;
	}
}
