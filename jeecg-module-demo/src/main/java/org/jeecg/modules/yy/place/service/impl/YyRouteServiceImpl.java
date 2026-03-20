package org.jeecg.modules.yy.place.service.impl;

import dm.jdbc.util.StringUtil;
import org.jeecg.config.yyi18n.LangContext;
import org.jeecg.modules.yy.place.entity.YyDiscount;
import org.jeecg.modules.yy.place.entity.YyRoute;
import org.jeecg.modules.yy.place.entity.YyRouteDetailPlaces;
import org.jeecg.modules.yy.place.entity.YyRoutePrice;
import org.jeecg.modules.yy.place.mapper.*;
import org.jeecg.modules.yy.place.service.IYyRouteDetailPlacesService;
import org.jeecg.modules.yy.place.service.IYyRouteService;
import org.jeecg.modules.yy.place.vo.RouteInfoView;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 行程情报
 * @Author: jeecg-boot
 * @Date:   2025-07-25
 * @Version: V1.0
 */
@Service
public class YyRouteServiceImpl extends ServiceImpl<YyRouteMapper, YyRoute> implements IYyRouteService {

	@Autowired
	private YyRouteMapper yyRouteMapper;
	@Autowired
	private YyRouteDetailMapper yyRouteDetailMapper;
	@Autowired
	private YyRoutePriceMapper yyRoutePriceMapper;
	@Autowired
	private YyUserReviewMapper yyUserReviewMapper;
	@Autowired
	private YyDiscountMapper yyDiscountMapper;
	@Autowired
	private YyUserFavoriteMapper yyUserFavoriteMapper;
	@Autowired
	private YyOrderMapper yyOrderMapper;
	@Autowired
	private YyRouteDetailPlacesMapper yyRouteDetailPlacesMapper;
	@Autowired
	private IYyRouteDetailPlacesService yyRouteDetailPlacesService;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		yyRouteDetailMapper.deleteByMainId(id);
		yyRoutePriceMapper.deleteByMainId(id);
		yyRouteMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			yyRouteDetailMapper.deleteByMainId(id.toString());
			yyRoutePriceMapper.deleteByMainId(id.toString());
			yyRouteMapper.deleteById(id);
		}
	}

	/**
	 * 各种行程一览取得
	 * 1.当前流行 -- 按季节推荐，后台可选择
	 * 2.综合热门 -- 按总订单量排序
	 * 3.顾客收藏热门 -- 按顾客收藏排序
	 * 4.我的收藏行程
	 *
	 * @param routeKbn
	 * @param routeNum
	 * @param customerId
	 */
	@Override
	public List<RouteInfoView> getRoutes(String routeKbn, int routeNum, String customerId) {
		List<RouteInfoView> resultList = new ArrayList<>();
		List<YyRoute> rets = new ArrayList<>();
		RouteInfoView routeInfo ;
		RouteInfoView reviewInfo ;
		YyRoutePrice routePrice;

		// 1.当前流行 -- 按季节推荐，后台可选择
		if ("1".equals(routeKbn)){
			// 后台选择的推荐行程取得
			rets = yyRouteMapper.selectByRecommendFlg("1",customerId);

		}else if ("2".equals(routeKbn)){
			// 2.综合热门 -- 按总订单量排序(默认:最近半年内，取得5条)
			rets = yyOrderMapper.selectTopOrderRoutes(180,routeNum,customerId);

		}else if ("3".equals(routeKbn)){
			// 3.顾客收藏热门 -- 按顾客收藏排序(默认:最近半年内，取得5条)
			rets = yyUserFavoriteMapper.selectTopFavoriteRoutes(180,routeNum,customerId);

		}else if ("4".equals(routeKbn)){
			// 4.我的收藏行程
			rets = yyUserFavoriteMapper.selectMyFavoriteRoutes(customerId);

		}

		if (rets != null && rets.size() > 0){
			for (YyRoute route : rets) {
				routeInfo = new RouteInfoView();
				routeInfo.setId(route.getId()); // 行程ID
				routeInfo.setImgUrl(route.getPicUrl()); // 行程代表图片
				routeInfo.setTitle(getResultByLang(route)); // 行程标题
				routeInfo.setPlaces(yyRouteDetailPlacesService.getPlacesByRouteId(route.getId())); // 行程地点（复数）
				reviewInfo = yyUserReviewMapper.getReviewInfoByRouteId(route.getId());
				if (reviewInfo != null) {
					routeInfo.setReviewRate(reviewInfo.getReviewRate()); // 用户评级
					routeInfo.setReviewNum(reviewInfo.getReviewNum()); // 评价用户数
				} else {
					routeInfo.setReviewRate(0); // 用户评级
					routeInfo.setReviewNum(0); // 评价用户数
				}

				BigDecimal basicPrice = yyRoutePriceMapper.getRouteBasicPrice("S5",route.getId()); // 舒适5座的价格作为基本价格
				if (basicPrice != null){
					routeInfo.setBasicPrice(basicPrice); // 基本价格
					routeInfo.setComputedPrice(computeDiscountPrice(basicPrice)); // 优惠价格
				} else {
					routeInfo.setBasicPrice(new BigDecimal(0)); // 基本价格
					routeInfo.setComputedPrice(new BigDecimal(0)); // 优惠价格
				}
				if (route.getFavoriteFlg() != null && !StringUtil.isEmpty(route.getFavoriteFlg())){
					routeInfo.setFavoriteFlg(route.getFavoriteFlg());
				}

				resultList.add(routeInfo);
			}
		}
		return resultList;
	}

	private String getResultByLang(YyRoute obj){
		String ret = "";

		if (obj != null) {
			switch (LangContext.getLang()) {
				case "zh-CN":
					ret = obj.getRouteTitleZh();
					break;
				case "zh-TW":
					ret = obj.getRouteTitleTw();
					break;
				case "ja":
					ret = obj.getRouteTitleJp();
					break;
				default:
					ret = obj.getRouteTitleEn();
					break;
			}
		}

		return ret;
	}

	/**
	 * 行程检索分页取得
	 *
	 * @param placeIds
	 * @param pageNum
	 * @param pageSize
	 */
	@Override
	public List<RouteInfoView> fetchRoutesPage(String[] placeIds, int pageNum, int pageSize, String userId) {
		List<RouteInfoView> resultList = new ArrayList<>();
		List<YyRoute> rets = new ArrayList<>();
		RouteInfoView routeInfo ;
		RouteInfoView reviewInfo ;
		YyRoutePrice routePrice;
		int offset = (pageNum - 1) * pageSize;
		int placeCount = placeIds.length;

		rets = yyRouteDetailPlacesMapper.selectRouteByPlacesPage(Arrays.asList(placeIds), offset, pageSize, placeCount,userId);

		if (rets != null && rets.size() > 0){
			for (YyRoute route : rets) {
				routeInfo = new RouteInfoView();
				routeInfo.setId(route.getId()); // 行程ID
				routeInfo.setImgUrl(route.getPicUrl()); // 行程代表图片
				routeInfo.setTitle(getResultByLang(route)); // 行程标题
				routeInfo.setPlaces(yyRouteDetailPlacesService.getPlacesByRouteId(route.getId())); // 行程地点（复数）
				reviewInfo = yyUserReviewMapper.getReviewInfoByRouteId(route.getId());
				if (reviewInfo != null) {
					routeInfo.setReviewRate(reviewInfo.getReviewRate()); // 用户评级
					routeInfo.setReviewNum(reviewInfo.getReviewNum()); // 评价用户数
				} else {
					routeInfo.setReviewRate(0); // 用户评级
					routeInfo.setReviewNum(0); // 评价用户数
				}
				BigDecimal basicPrice = yyRoutePriceMapper.getRouteBasicPrice("04",route.getId()); // 舒适5座的价格作为基本价格
				if (basicPrice != null){
					routeInfo.setBasicPrice(basicPrice); // 基本价格
					routeInfo.setComputedPrice(computeDiscountPrice(basicPrice)); // 优惠价格
				} else {
					routeInfo.setBasicPrice(new BigDecimal(0)); // 基本价格
					routeInfo.setComputedPrice(new BigDecimal(0)); // 优惠价格
				}
				if (route.getFavoriteFlg() != null && !StringUtil.isEmpty(route.getFavoriteFlg())){
					routeInfo.setFavoriteFlg(route.getFavoriteFlg());
				}
				resultList.add(routeInfo);
			}
		}
		return resultList;
	}

	private BigDecimal computeDiscountPrice(BigDecimal basic){
		BigDecimal computedPrice = basic;
		BigDecimal rate = new BigDecimal(0.00);

		// 如果当前有系统优惠，计算优惠后价格
		List<YyDiscount> sysDiscounts = yyDiscountMapper.getValidDiscount("01");
		if (sysDiscounts != null && sysDiscounts.size() > 0){
			for(YyDiscount discount : sysDiscounts){
				// 减额优惠
				if ("01".equals(discount.getDiscountKbn())) {
					computedPrice = computedPrice.subtract(discount.getDiscountNum());
				}
				// 比例优惠
				if ("02".equals(discount.getDiscountKbn())) {
					rate = new BigDecimal(1).subtract(new BigDecimal(discount.getDiscountRate()).divide(new BigDecimal(100)));
					computedPrice = computedPrice.multiply(rate);
				}
			}
		}

		return computedPrice;
	}
}
