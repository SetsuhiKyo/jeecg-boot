package org.jeecg.modules.yy.place.controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.yyi18n.LangContext;
import org.jeecg.modules.yy.api.GoogleRoutesAPIV2;
import org.jeecg.modules.yy.api.RoutesResponse;
import org.jeecg.modules.yy.place.entity.YyPlace;
import org.jeecg.modules.yy.place.entity.YyPlaceDistance;
import org.jeecg.modules.yy.place.entity.YyRoute;
import org.jeecg.modules.yy.place.entity.YyRouteDetailPlaces;
import org.jeecg.modules.yy.place.mapper.YyPlaceDistanceMapper;
import org.jeecg.modules.yy.place.service.IYyPlaceDistanceService;
import org.jeecg.modules.yy.place.service.IYyPlaceService;
import org.jeecg.modules.yy.place.service.IYyRouteDetailPlacesService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.yy.place.service.IYyRouteService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

 /**
 * @Description: 行程明细经由地点
 * @Author: jeecg-boot
 * @Date:   2025-07-29
 * @Version: V1.0
 */
@Api(tags="行程明细经由地点")
@RestController
@RequestMapping("/place/yyRouteDetailPlaces")
@Slf4j
public class YyRouteDetailPlacesController extends JeecgController<YyRouteDetailPlaces, IYyRouteDetailPlacesService> {
	 private static final int ROUND_UNIT_SECONDS = 30 * 60; // 1800
	 private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	 @Autowired
	private IYyRouteDetailPlacesService yyRouteDetailPlacesService;
	 @Autowired
	 private IYyPlaceService yyPlaceService;
	 @Autowired
	 private IYyPlaceDistanceService yyPlaceDistanceService;
	 @Autowired
	 private GoogleRoutesAPIV2 routeApi;
	 @Autowired
	 private YyPlaceDistanceMapper yyPlaceDistanceMapper;
	 @Autowired
	 private IYyRouteService yyRouteService;
	/**
	 * 分页列表查询
	 *
	 * @param yyRouteDetailPlaces
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "行程明细经由地点-分页列表查询")
	@ApiOperation(value="行程明细经由地点-分页列表查询", notes="行程明细经由地点-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<YyRouteDetailPlaces>> queryPageList(YyRouteDetailPlaces yyRouteDetailPlaces,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<YyRouteDetailPlaces> queryWrapper = QueryGenerator.initQueryWrapper(yyRouteDetailPlaces, req.getParameterMap());
		Page<YyRouteDetailPlaces> page = new Page<YyRouteDetailPlaces>(pageNo, pageSize);
		IPage<YyRouteDetailPlaces> pageList = yyRouteDetailPlacesService.page(page, queryWrapper);

		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param yyRouteDetailPlaces
	 * @return
	 */
	@AutoLog(value = "行程明细经由地点-添加")
	@ApiOperation(value="行程明细经由地点-添加", notes="行程明细经由地点-添加")
//	@RequiresPermissions("place:yy_route_detail_places:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody YyRouteDetailPlaces yyRouteDetailPlaces) {
		int branchNo = yyRouteDetailPlaces.getBranchNo();
		String placeId = yyRouteDetailPlaces.getPassPlaceId();
		YyRouteDetailPlaces prvPlace = new YyRouteDetailPlaces();
		int distance =0;
		int duration = 0;

		if (branchNo > 1 ){
			String routeId = yyRouteDetailPlaces.getRouteId();
			String routeDetailId = yyRouteDetailPlaces.getDetailId();
			String fromPlaceId = "";
			String toPlaceId = "";
			String prvPlaceId = "";
			YyPlaceDistance placeDistance = new YyPlaceDistance();

			// 前一个地点ID取得
			prvPlace = yyRouteDetailPlacesService.getPrvPlace(routeId,routeDetailId,branchNo-1);
			prvPlaceId = prvPlace.getPassPlaceId();

			// 地点ID比较 小->大的地点对登录
			if (Long.parseLong(prvPlaceId) < Long.parseLong(placeId)){
				fromPlaceId = prvPlaceId;
				toPlaceId = placeId;
			} else {
				fromPlaceId = placeId;
				toPlaceId = prvPlaceId;
			}

			// 判断地点对是否登录
			boolean ret = true;
			 ret = yyPlaceDistanceService.isPlaceIdRegisted(fromPlaceId,toPlaceId);
			if (!ret) {
				YyPlace fromPlace = yyPlaceService.getById(fromPlaceId);
				YyPlace toPlace = yyPlaceService.getById(toPlaceId);
				RoutesResponse res = routeApi.computeRouteByPlaceId(fromPlace.getGooglePladeId(), toPlace.getGooglePladeId());
				placeDistance.setStartPlace(fromPlaceId); // 开始地点ID
				placeDistance.setEndPlace(toPlaceId); // 终了地点ID

				if (res.getRoutes() != null && res.getRoutes().size()>0){
					distance = Integer.parseInt(res.getRoutes().get(0).getDistanceMeters().split("m")[0]);
					duration = Integer.parseInt(res.getRoutes().get(0).getDuration().split("s")[0]);
				} else {
					return Result.error("俩个地点之间无法计算距离，请重新选择一个地点登录！");
				}
				placeDistance.setDistance(distance); // 距离：米
				placeDistance.setDuration(duration); // 时间：秒
				placeDistance.setPolyline(res.getRoutes().get(0).getPolyline().getEncodedPolyline()); // 路线：Google返回字符串
				placeDistance.setDataSource("ROUTE"); // 来源
				placeDistance.setHitCount(0);
				yyPlaceDistanceService.save(placeDistance);
			} else {
				distance = yyPlaceDistanceMapper.getResultByFromTo(fromPlaceId, toPlaceId);
			}

			// 当前地点对的距离追加到行程中
			YyRoute route = yyRouteService.getById(routeId);
			distance += route.getTotalDistance();
			route.setTotalDistance(distance);
			yyRouteService.save(route);
		}
		YyPlace yyPlace = yyPlaceService.getById(placeId);
		yyRouteDetailPlaces.setPassPlaceNm(getResultByLang(yyPlace));
		// 自动计算：到达下一个地点的时间
//		if (branchNo > 1 ) {
//			yyRouteDetailPlaces.setArriveTime(computeArriveTime(prvPlace.getArriveTime(),Integer.parseInt(duration.split("s")[0])));
//		}
		yyRouteDetailPlacesService.save(yyRouteDetailPlaces);
		return Result.OK("添加成功！");
	}

	 private String getResultByLang(YyPlace obj){
		 String ret = "";

		 switch (LangContext.getLang()){
			 case "zh-CN":
				 ret = obj.getNameZh();
				 break;
			 case "zh-TW":
				 ret = obj.getNameTw();
				 break;
			 case "ja":
				 ret = obj.getNameJp();
				 break;
			 default:
				 ret = obj.getNameEn();
				 break;
		 }

		 return ret;
	 }

		 /**
		  *  行程中根据前一个地点的到达时间和到达下一个地点的花费时间
		  *  计算出到达下一个地点的大致时间（四舍五入）
		  *
		  * @param prvArriveTime 前一个地点的到达时间
		  * @param arriveNextSeconds 到达下一个地点的花费时间
		  * @return 到达下一个地点的大致时间
		  */
//		 public static String computeArriveTime(String prvArriveTime, int arriveNextSeconds) {
//			 // 1. 解析时间
//			 LocalTime time = LocalTime.parse(prvArriveTime, FORMATTER);
//
//			 // 2. 转为当天的秒数
//			 long totalSeconds =
//					 time.toSecondOfDay() + arriveNextSeconds;
//
//			 // 3. 防止超过 24 小时
//			 totalSeconds = totalSeconds % (24 * 3600);
//
//			 // 4. 30 分钟向上进位
//			 long roundedSeconds =
//					 ((totalSeconds + ROUND_UNIT_SECONDS - 1) / ROUND_UNIT_SECONDS)
//							 * ROUND_UNIT_SECONDS;
//
//			 // 5. 再次防止进位后溢出
//			 roundedSeconds = roundedSeconds % (24 * 3600);
//
//			 // 6. 转回 HH:mm
//			 return LocalTime.ofSecondOfDay(roundedSeconds).format(FORMATTER);
//		 }

	 /**
	 *  编辑
	 *
	 * @param yyRouteDetailPlaces
	 * @return
	 */
	@AutoLog(value = "行程明细经由地点-编辑")
	@ApiOperation(value="行程明细经由地点-编辑", notes="行程明细经由地点-编辑")
//	@RequiresPermissions("place:yy_route_detail_places:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody YyRouteDetailPlaces yyRouteDetailPlaces) {
		YyPlace yyPlace = yyPlaceService.getById(yyRouteDetailPlaces.getPassPlaceId());
		yyRouteDetailPlaces.setPassPlaceNm(getResultByLang(yyPlace));
		yyRouteDetailPlacesService.updateById(yyRouteDetailPlaces);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "行程明细经由地点-通过id删除")
	@ApiOperation(value="行程明细经由地点-通过id删除", notes="行程明细经由地点-通过id删除")
//	@RequiresPermissions("place:yy_route_detail_places:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		yyRouteDetailPlacesService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "行程明细经由地点-批量删除")
	@ApiOperation(value="行程明细经由地点-批量删除", notes="行程明细经由地点-批量删除")
//	@RequiresPermissions("place:yy_route_detail_places:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.yyRouteDetailPlacesService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "行程明细经由地点-通过id查询")
	@ApiOperation(value="行程明细经由地点-通过id查询", notes="行程明细经由地点-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<YyRouteDetailPlaces> queryById(@RequestParam(name="id",required=true) String id) {
		YyRouteDetailPlaces yyRouteDetailPlaces = yyRouteDetailPlacesService.getById(id);
		if(yyRouteDetailPlaces==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(yyRouteDetailPlaces);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param yyRouteDetailPlaces
    */
    @RequiresPermissions("place:yy_route_detail_places:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, YyRouteDetailPlaces yyRouteDetailPlaces) {
        return super.exportXls(request, yyRouteDetailPlaces, YyRouteDetailPlaces.class, "行程明细经由地点");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("place:yy_route_detail_places:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, YyRouteDetailPlaces.class);
    }

}
