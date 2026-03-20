package org.jeecg.modules.yy.place.controller;

import java.math.BigDecimal;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.config.yyi18n.LangContext;
import org.jeecg.modules.yy.place.OrderStatus;
import org.jeecg.modules.yy.place.PaymentStatus;
import org.jeecg.modules.yy.place.entity.*;
import org.jeecg.modules.yy.place.service.*;
import org.jeecg.modules.yy.place.vo.*;
import org.jeecg.modules.yy.utils.Base62Util;
import org.jeecg.modules.yy.utils.CalPriceUtilServiceImpl;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;


 /**
 * @Description: 车务订单
 * @Author: jeecg-boot
 * @Date:   2025-07-24
 * @Version: V1.0
 */
@Api(tags="车务订单")
@RestController
@RequestMapping("/place/yyOrder")
@Slf4j
public class YyOrderController extends JeecgController<YyOrder, IYyOrderService> {
	 @Autowired
	 private IYyPlaceService yyPlaceService;
	@Autowired
	private IYyOrderService yyOrderService;
	@Autowired
	private IYyOrderReviewService yyOrderReviewService;
	 @Autowired
	private IYyDiscountService yyDiscountService;
	 @Autowired
	 private IYyUserDiscountService yyUserDiscountService;
	 @Autowired
	 private IYyRouteDetailPlacesService yyRouteDetailPlacesService;
	 @Autowired
	 private IYyPlaceDistanceService yyPlaceDistanceService;
	 @Autowired
	 private CalPriceUtilServiceImpl calPriceUtil;
	 @Autowired
	 private IYyRouteService yyRouteService;

	
	/**
	 * 分页列表查询
	 *
	 * @param yyOrder
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "车务订单-分页列表查询")
	@ApiOperation(value="车务订单-分页列表查询", notes="车务订单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<YyOrder>> queryPageList(YyOrder yyOrder,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        // 自定义查询规则
        Map<String, QueryRuleEnum> customeRuleMap = new HashMap<>();
        // 自定义多选的查询规则为：LIKE_WITH_OR
		customeRuleMap.put("orderType", QueryRuleEnum.LIKE_WITH_OR);
        customeRuleMap.put("orderKbn", QueryRuleEnum.LIKE_WITH_OR);
        customeRuleMap.put("orderSts", QueryRuleEnum.LIKE_WITH_OR);
        QueryWrapper<YyOrder> queryWrapper = QueryGenerator.initQueryWrapper(yyOrder, req.getParameterMap(),customeRuleMap);
		Page<YyOrder> page = new Page<YyOrder>(pageNo, pageSize);
		IPage<YyOrder> pageList = yyOrderService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	 /**
	  *   添加
	  *
	  * @param yyOrder
	  * @return
	  */
	 @AutoLog(value = "车务订单-添加")
	 @ApiOperation(value="车务订单-添加", notes="车务订单-添加")
	 @RequiresPermissions("place:yy_order:add")
	 @PostMapping(value = "/add")
	 public Result<String> add(@RequestBody YyOrder yyOrder) {
		 yyOrderService.save(yyOrder);
		 return Result.OK("添加成功！");
	 }

	 /**
	  *  编辑
	  *
	  * @param yyOrder
	  * @return
	  */
	 @AutoLog(value = "车务订单-编辑")
	 @ApiOperation(value="车务订单-编辑", notes="车务订单-编辑")
	 @RequiresPermissions("place:yy_order:edit")
	 @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	 public Result<String> edit(@RequestBody YyOrder yyOrder) {
		 yyOrderService.updateByOrderInfo(yyOrder);
		 return Result.OK("编辑成功!");
	 }

	 /**
	  *   通过id删除
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "车务订单-通过id删除")
	 @ApiOperation(value="车务订单-通过id删除", notes="车务订单-通过id删除")
	 @RequiresPermissions("place:yy_order:delete")
	 @DeleteMapping(value = "/delete")
	 public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		 yyOrderService.removeById(id);
		 return Result.OK("删除成功!");
	 }

	 /**
	  *  批量删除
	  *
	  * @param ids
	  * @return
	  */
	 @AutoLog(value = "车务订单-批量删除")
	 @ApiOperation(value="车务订单-批量删除", notes="车务订单-批量删除")
	 @RequiresPermissions("place:yy_order:deleteBatch")
	 @DeleteMapping(value = "/deleteBatch")
	 public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		 this.yyOrderService.removeByIds(Arrays.asList(ids.split(",")));
		 return Result.OK("批量删除成功!");
	 }
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "车务订单-通过id查询")
	@ApiOperation(value="车务订单-通过id查询", notes="车务订单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<YyOrder> queryById(@RequestParam(name="id",required=true) String id) {
		YyOrder yyOrder = yyOrderService.getById(id);
		if(yyOrder==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(yyOrder);

	}
	
    /**
    * 导出excel
    *
    * @param request
    * @param yyOrder
    */
    @RequiresPermissions("place:yy_order:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, YyOrder yyOrder) {
		return super.exportXls(request, yyOrder, YyOrder.class, "车务订单");
    }

    /**
    * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("place:yy_order:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
		return super.importExcel(request, response, YyOrder.class);
    }

	 /**
	  * 用户评价取得
	  *
	  * @param reviewNum
	  * @return
	  */
	 @ApiOperation(value="用户评价取得", notes="用户评价取得")
	 @GetMapping(value = "/getUserReviews")
	 public Result<List<ReviewInfoView>> getUserReviews(@RequestParam(name="reviewNum",required=true) String reviewNum) {

		 Result<List<ReviewInfoView>> result = new Result<>();
		 try {
			 List<ReviewInfoView> topRouteInfoList = new ArrayList<>();
			 ReviewInfoView topRouteItem = new ReviewInfoView();
			 topRouteItem.setUserId("U5682532");
			 topRouteItem.setUserName("jiangxuefeijiangxu");
			 topRouteItem.setCountryImg("../../static/app/flag-china.png");
			 topRouteItem.setReviewTime("3天前");
			 topRouteItem.setRouteId("202109291014243117");
			 topRouteItem.setRouteTitle("东京-镰仓往返一日游");
			 topRouteItem.setRouteReview(4.7);
			 topRouteItem.setReviewContent("镰仓往返镰仓往返镰仓往返镰仓往返周边一日游富士山与周边一日游");
			 topRouteInfoList.add(topRouteItem);

//			 topRouteItem = new UserReviewInfo();
//			 topRouteItem.setUserId("U5682532");
//			 topRouteItem.setUserName("jiangxuefei");
//			 topRouteItem.setCountryImg("../../static/app/flag-china.png");
//			 topRouteItem.setReviewTime("刚刚");
//			 topRouteItem.setRouteId("202109291014243117");
//			 topRouteItem.setRouteTitle("东京-富士山一日游");
//			 topRouteItem.setRouteReview(3.5);
//			 topRouteItem.setReviewContent("富士山与周边一日游富士山与周边一日游富士山与周边一日游富士山与周边一日游富士山与周边一日游");
//			 topRouteInfoList.add(topRouteItem);


			 result.setResult(topRouteInfoList);
			 result.setSuccess(true);
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 result.setSuccess(false);
			 result.setMessage("查询失败");
		 }
		 return result;
	 }

	 /**
	  * 订单价格计算
	  *
	  * @param orderInfo
	  * @return
	  */
	 @PostMapping(value = "/computePrice")
	 public Result<JSONObject> computePrice(@RequestBody OrderInfoView orderInfo, HttpServletRequest request) {
		 Result<JSONObject> result = new Result<>();
		 JSONObject obj = new JSONObject();
		 // 订单种别
		 String orderType = orderInfo.getOrderType();
		 // 订单区分
		 String orderKbn = orderInfo.getOrderKbn();

		 // 系统优惠
		 List<DiscountInfo> activeDis = new ArrayList<>();
		 activeDis = yyDiscountService.getSysDiscount();

		 // 顾客优惠
		 List<DiscountInfo> myDis = new ArrayList<>();
		 myDis = yyUserDiscountService.getCustomerDiscount(orderInfo.getCustomerId());

		 // 用车距离
		 int orderDistance = 0;
		 // 用车时间
		 int orderHours = 0;
		 // 行程名称
		 String routeNm = "";

		 // 机场接送订单
		 if ("01".equals(orderType)) {
			 orderDistance = yyPlaceDistanceService.getDistanceByFromTO(orderInfo.getPickupLoc(),orderInfo.getDropLoc());
		 } else if ("02".equals(orderType)) {
			 // 一般接送订单
			 orderDistance = yyPlaceDistanceService.getDistanceByFromTO(orderInfo.getPickupLoc(), orderInfo.getDropLoc());
			 int rtnDistance = 0;
			 if ("4".equals(orderKbn)) {
				 rtnDistance = yyPlaceDistanceService.getDistanceByFromTO(orderInfo.getRtnPickupLoc(), orderInfo.getRtnDorpLoc());
			 }
			 orderDistance += rtnDistance;
		 } else if ("03".equals(orderType)) {
			 // 按小时包车订单
			 orderHours = orderInfo.getUseHours();
		 } else if ("04".equals(orderType)) {
			 // 按行程包车订单
			 YyRoute selectedRoute = yyRouteService.getById(orderInfo.getRouteId());
			 orderDistance = selectedRoute.getTotalDistance();
			 routeNm = getResultByLang(selectedRoute);
		 }

		 BigDecimal priceTaxi = new BigDecimal(0);
		 BigDecimal priceBasic = new BigDecimal(0);
		 int distanceKm = meterToKm(orderDistance);
		 try{
			 // 出租价格
			 priceTaxi = calPriceUtil.calTaxiPrice(distanceKm);
			 String browserLang = LangContext.getLang();
			 // 基本价格
			 priceBasic = calPriceUtil.calBasicPrice(orderInfo,distanceKm,browserLang);
		 } catch (Exception e) {
		 }

		 obj.put("routeDistance", distanceKm); // 用车距离
		 obj.put("activeDiscont", activeDis); // 系统优惠
		 obj.put("myDiscont", myDis); // 我的优惠
		 obj.put("priceTaxi", priceTaxi); // 出租价格
		 obj.put("priceBasic", priceBasic); // 基本价格
		 obj.put("routeName", routeNm); // 行程名称
		 result.setResult(obj);
		 result.setSuccess(true);
		 return result;
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
	  * 订单登录
	  *
	  * @return
	  */
	 @PostMapping(value = "/addOrder")
	 public Result<JSONObject> addOrder(@RequestBody OrderInfoView orderInfo, HttpServletRequest request) {
		 Result<JSONObject> result = new Result<>();
		 JSONObject obj = new JSONObject();

		 // 订单登录
		 String orderNo = yyOrderService.registOrderAndPlaceDistance(orderInfo);

		 obj.put("orderNo", Base62Util.encode(orderNo)); // 订单编号
		 obj.put("customerEmail", ""); // 顾客邮件
		 result.setResult(obj);
		 result.setSuccess(true);
		 return result;
	 }

	 /**
	  * 订单取消
	  *
	  * @return
	  */
	 @GetMapping(value = "/cancelOrder")
	 public Result<JSONObject> cancelOrder(@RequestParam(name="orderId",required=true) String orderId) {
		 Result<JSONObject> result = new Result<>();
		 JSONObject obj = new JSONObject();

		 // 订单取消
		 boolean updateRet = yyOrderService.updateByOrderStatus(Base62Util.decode(orderId),OrderStatus.CANCELLED.name());

		 result.setResult(obj);
		 result.setSuccess(updateRet);
		 return result;
	 }

	 /**
	  * 订单服务完成
	  *
	  * @return
	  */
	 @GetMapping(value = "/completeOrder")
	 public Result<JSONObject> completeOrder(@RequestParam(name="orderId",required=true) String orderId) {
		 Result<JSONObject> result = new Result<>();
		 JSONObject obj = new JSONObject();

		 // 订单完成
		 boolean updateRet = yyOrderService.updateByOrderStatus(Base62Util.decode(orderId),OrderStatus.COMPLETED.name());

		 result.setResult(obj);
		 result.setSuccess(updateRet);
		 return result;
	 }

	 /**
	  * 支付确认
	  *
	  * @return
	  */
	 @GetMapping(value = "/paiedConfirm")
	 public Result<JSONObject> paiedConfirm(@RequestParam(name="orderId",required=true) String orderId) {
		 Result<JSONObject> result = new Result<>();
		 JSONObject obj = new JSONObject();

		 // 支付确认
		 boolean updateRet = yyOrderService.updateByPaymentSts(Base62Util.decode(orderId), PaymentStatus.PENDING_CONFIRM.name());

		 result.setResult(obj);
		 result.setSuccess(updateRet);
		 return result;
	 }

	 /**
	  * 用户订单取得
	  *
	  * @param userId
	  * @return
	  */
	 @ApiOperation(value="用户订单取得", notes="用户订单取得")
	 @GetMapping(value = "/getMyOrder")
	 public Result<List<OrderInfoView>> getMyOrder(@RequestParam(name="userId",required=true) String userId) {

		 Result<List<OrderInfoView>> result = new Result<>();
		 List<OrderInfoView> myOrders = new ArrayList<>();

		 try {
			 myOrders = yyOrderService.getCustomerOrder(userId);
			 String orderId = "";
			 if(myOrders != null && myOrders.size() > 0 ){
				 for (OrderInfoView order:myOrders){
					 orderId = order.getOrderNo();
					 order.setOrderNo(Base62Util.encode(orderId));
				 }
			 }
			 result.setResult(myOrders);
			 result.setSuccess(true);
		 } catch (Exception e) {
			 log.error(e.getMessage(),e);
			 result.setSuccess(false);
			 result.setMessage("查询失败");
		 }
		 return result;
	 }



	 /**
	  * 把米转换成公里
	  *
	  */
	 private int meterToKm(int meter) {
		 return (int) Math.round(meter / 1000.0);
	 }
}
