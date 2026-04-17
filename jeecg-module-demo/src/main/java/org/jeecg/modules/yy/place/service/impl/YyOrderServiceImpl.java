package org.jeecg.modules.yy.place.service.impl;

import com.alibaba.fastjson.JSONObject;
import dm.jdbc.util.StringUtil;
import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.enums.EmailTemplateEnum;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.config.yyi18n.LangContext;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.yy.api.GooglePlacesAPIV1;
import org.jeecg.modules.yy.api.GoogleRoutesAPIV2;
import org.jeecg.modules.yy.api.RoutesResponse;
import org.jeecg.modules.yy.events.*;
import org.jeecg.modules.yy.place.OrderStatus;
import org.jeecg.modules.yy.place.PaymentStatus;
import org.jeecg.modules.yy.place.RefundStatus;
import org.jeecg.modules.yy.place.SettlementStatus;
import org.jeecg.modules.yy.place.entity.*;
import org.jeecg.modules.yy.place.mapper.*;
import org.jeecg.modules.yy.place.service.*;
import org.jeecg.modules.yy.place.vo.OrderInfoView;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Description: 车务订单
 * @Author: jeecg-boot
 * @Date:   2025-07-24
 * @Version: V1.0
 */
@Service
public class YyOrderServiceImpl extends ServiceImpl<YyOrderMapper, YyOrder> implements IYyOrderService {

	@Autowired
	private IYyPlaceService yyPlaceService;
	@Autowired
	private YyOrderMapper yyOrderMapper;
	@Autowired
	private IYyRouteService yyRouteService;
	@Autowired
	private YyPlaceMapper yyPlaceMapper;
	@Autowired
	private IYyPlaceDistanceService yyPlaceDistanceService;
	@Autowired
	private GoogleRoutesAPIV2 routeApi;
	@Autowired
	private GooglePlacesAPIV1 googlePlacesApi;
	@Autowired
	private IYyOrderSettlementService yyOrderSettlementService;
	@Autowired
	private IYyOrderRefundService yyOrderRefundService;
	@Autowired
	private ApplicationEventPublisher publisher;
	/**
	 * 使用用户ID取得订单一览
	 *
	 * @param userId
	 */
	@Override
	public List<OrderInfoView> getCustomerOrder(String userId) {
		List<OrderInfoView> userOrders = new ArrayList<>();
		OrderInfoView orderInfo;

		userOrders= yyOrderMapper.getCustomerOrder(userId);

		return userOrders;
	}

	/**
	 * 登录订单及地点距离登录
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public String registOrderAndPlaceDistance(OrderInfoView orderInfo) {
		// 登录订单
		YyOrder order = registOrder(orderInfo);

		if (order != null) {
			// 地点距离登录
			// 判断地点对是否登录
			if ("01".equals(orderInfo.getOrderType()) || "02".equals(orderInfo.getOrderType())) {
				registPlaceDistance(orderInfo.getPickupLoc(), orderInfo.getDropLoc());
				if ("4".equals(orderInfo.getOrderKbn())) {
					registPlaceDistance(orderInfo.getRtnPickupLoc(), orderInfo.getRtnDorpLoc());
				}
			}

			// 发送下单成功邮件
			publisher.publishEvent(new OrderCreatedEvent(order));

			return order.getId();
		} else {
			return "";
		}

	}

	/**
	 * 后台更新订单情报
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateByOrderInfo(YyOrder orderInfo){
		boolean updateOrderSts = false;
		boolean updatePaymentSts = false;

		YyOrder dbOrder = getById(orderInfo.getId());
		String orderSts = orderInfo.getOrderSts();
		String paymentSts = orderInfo.getPaymentSts();

		orderInfo.setOrderSts(dbOrder.getOrderSts());
		orderInfo.setPaymentSts(dbOrder.getPaymentSts());

		updateById(orderInfo);

		if (!orderSts.equals(dbOrder.getOrderSts())) {
			updateOrderSts = updateOrderSts(orderInfo.getId(),orderSts);
		}

		if (!paymentSts.equals(dbOrder.getPaymentSts())) {
			updatePaymentSts = updatePaymentSts(orderInfo.getId(),paymentSts);
		}

		return updateOrderSts && updatePaymentSts;
	}

	/**
	 * 前台更新订单状态
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateByOrderStatus(String orderId,String orderSts){

		return updateOrderSts(orderId,orderSts);
	}

	/**
	 * 更新订单状态
	 */
	private boolean updateOrderSts(String orderId, String orderSts) {
		boolean updateRet = false;
		YyOrder order = getById(orderId);
		Object targetEvent = "";

		switch (orderSts) {
			case "CONFIRMED":
				if (!OrderStatus.CREATED.name().equals(order.getOrderSts())) {
					throw new RuntimeException("当前状态不能更新为【已确认】,请重新创建订单");
				}
				order.setOrderSts(OrderStatus.CONFIRMED.name());
				targetEvent = new OrderConfirmedEvent(order);
				break;
			case "READY":
				if (!PaymentStatus.PAID.name().equals(order.getPaymentSts())) {
					throw new RuntimeException("当前状态不能更新为【待服务】，请先确认订单【已支付】");
				}
				order.setOrderSts(OrderStatus.READY.name());
				targetEvent = new ServiceReminderEvent(order);
				break;
			case "IN_SERVICE":
				if (!OrderStatus.READY.name().equals(order.getOrderSts())) {
					throw new RuntimeException("当前状态不能更新为【服务中】，请先确认订单【待服务】");
				}
				order.setOrderSts(OrderStatus.IN_SERVICE.name());
				// TODO:给客服发送【正在服务】邮件，现实点未做成
//				targetEvent = new ServiceInProcessEvent(order);
				break;
			case "COMPLETED":
				if (!OrderStatus.IN_SERVICE.name().equals(order.getOrderSts())) {
					throw new RuntimeException("当前状态不能更新为【服务完成】，请先确认司机已在【服务中】");
				}
				order.setOrderSts(OrderStatus.COMPLETED.name());
				targetEvent = new ServiceCompletedEvent(order);
				// TODO:发起结算流程
				// 发起结算申请流程，在结算表中新增一条记录
//				YyOrderSettlement settlement  = new YyOrderSettlement();
//				settlement.setOrderId(orderId);
//				settlement.setSettlementSts(SettlementStatus.REQUESTED.name());
//				yyOrderSettlementService.save(settlement);

				break;
			case "CANCEL_REQUESTED":
			case "CANCELLED":
				if (OrderStatus.IN_SERVICE.name().equals(order.getOrderSts())
				|| OrderStatus.COMPLETED.name().equals(order.getOrderSts())) {
					throw new RuntimeException("当前状态不能【申请取消】，因为订单已在服务中或已完成服务。");
				}
				if (OrderStatus.CREATED.name().equals(order.getOrderSts())
						|| (OrderStatus.CONFIRMED.name().equals(order.getOrderSts())
						&& PaymentStatus.UNPAID.name().equals(order.getPaymentSts()))) {
					// 订单在【已预约】，【已确认】但【未支付】的情况下，直接更新为【已取消】，邮件中不提及扣除违约金
					order.setOrderSts(OrderStatus.CANCELLED.name());
					targetEvent = new OrderCancelledEvent(order,false);
				}
				if (OrderStatus.CONFIRMED.name().equals(order.getOrderSts())
						&& PaymentStatus.PENDING_CONFIRM.name().equals(order.getPaymentSts())) {
					// 顾客更新为【支付确认】的情况下，首先更新为【申请取消】状态，并向客服发送邮件。
					// 客服线下确认是否收到汇款，如果收到汇款，在后台界面更新为【已支付】，再更新为【已取消】。
					// 如果未收到汇款（需要等几天再确认几次，还是没有收到的话），在后台界面更新为【未支付】，再更新【已取消】。
					order.setOrderSts(OrderStatus.CANCEL_REQUESTED.name());
					targetEvent = new RequestCancelEvent(order);
				}
				if (OrderStatus.READY.name().equals(order.getOrderSts())) {
					// 订单在【待服务】的情况下，直接更新为【申请取消】，邮件中需要提及扣除违约金
					order.setOrderSts(OrderStatus.CANCEL_REQUESTED.name());
					targetEvent = new RequestCancelEvent(order);
					// TODO:发起退款流程，在退款表中新增一条记录
//					YyOrderRefund refund  = new YyOrderRefund();
//					refund.setOrderId(orderId);
//					refund.setRefundSts(RefundStatus.REQUESTED.name());
//					refund.setRefundAmount(order.getServerPrice().multiply(new BigDecimal(0.3))); // 已经准备服务的情况下取消订单，扣除30%违约金。
//					yyOrderRefundService.save(refund);

				}
				if (OrderStatus.CANCEL_REQUESTED.name().equals(order.getOrderSts())) {
					if (PaymentStatus.PAID.name().equals(order.getPaymentSts())) {
						// 【已支付】的情况下，直接更新为【已取消】，发送邮件，说明扣除违约金
						targetEvent = new OrderCancelledEvent(order,true);
						// TODO:发起退款流程，在退款表中新增一条记录
//						YyOrderRefund refund  = new YyOrderRefund();
//						refund.setOrderId(orderId);
//						refund.setRefundSts(RefundStatus.REQUESTED.name());
//						refund.setRefundAmount(order.getServerPrice().multiply(new BigDecimal(0.1))); // 已经准备服务的情况下取消订单，扣除10%违约金。
//						yyOrderRefundService.save(refund);

					}
					if (PaymentStatus.UNPAID.name().equals(order.getPaymentSts())) {
						// 【未支付】的情况下，直接更新为【已取消】，发送邮件，不扣除违约金
						targetEvent = new OrderCancelledEvent(order,false);
					}
					order.setOrderSts(OrderStatus.CANCELLED.name());
				}
				break;
		}

		updateRet = saveOrUpdate(order);
		if (updateRet && !targetEvent.toString().isEmpty()) {
			publisher.publishEvent(targetEvent);
		}

		return updateRet;
	}

	/**
	 * 前台更新支付状态
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateByPaymentSts(String orderId,String paymentSts){

		return updatePaymentSts(orderId,paymentSts);
	}

	/**
	 * 更新支付状态
	 */
	private boolean updatePaymentSts(String orderId, String paymentSts) {
		boolean updateRet = false;
		YyOrder order = getById(orderId);
		Object targetEvent = "";

		switch (paymentSts) {
			case "PENDING_CONFIRM":
				if (!OrderStatus.CONFIRMED.name().equals(order.getOrderSts())) {
					throw new RuntimeException("当前状态不能更新为【支付确认】,请先【已确认】订单。");
				}
				order.setPaymentSts(PaymentStatus.PENDING_CONFIRM.name());
				// 给客服发送【顾客已支付】邮件
				targetEvent = new PaymentPaiedConfirmEvent(order);
				break;
			case "PAID":
				if (!PaymentStatus.PENDING_CONFIRM.name().equals(order.getPaymentSts())) {
					throw new RuntimeException("当前状态不能更新为【已支付】,先请顾客转账，然后发起【支付确认】。");
				}
				order.setPaymentSts(PaymentStatus.PAID.name());

				break;
		}

		updateRet = saveOrUpdate(order);

		if(updateRet && PaymentStatus.PAID.name().equals(order.getPaymentSts())) {
			// 确认【已支付】更新完成后，直接更新订单为【待服务】
			updateOrderSts(orderId, OrderStatus.READY.name());
		}

		if (updateRet) {
			publisher.publishEvent(targetEvent);
		}
		return updateRet;
	}

	/**
	 * 登录订单
	 */

	private YyOrder registOrder(OrderInfoView orderInfo) {
		YyOrder order = new YyOrder();
		order.setCustomerId(orderInfo.getCustomerId()); // 顾客用户ID
		order.setOrderType(orderInfo.getOrderType()); // 订单种别
		order.setOrderKbn(orderInfo.getOrderKbn()); // 订单区分
		order.setOrderSts(OrderStatus.CREATED.name()); // 订单状态 CREATED：已预约
		order.setPaymentSts(PaymentStatus.UNPAID.name()); // 支付状态 UNPAID：未支付
		order.setOrderName(makeOrderNm(orderInfo)); // 订单名称
		order.setRouteId(orderInfo.getRouteId()); // 行程ID
		order.setStartTime(formatterTime(orderInfo.getPickupDte())); // 开始日时
		order.setPickupPlaceId(getPlaceId(orderInfo.getPickupLoc())); //  上车地点ID
		order.setDropPlaceId(getPlaceId(orderInfo.getDropLoc())); // 终了地点ID
		order.setFlightNo(orderInfo.getFlightNo()); // 航班号
		order.setReturnTime(formatterTime(orderInfo.getReturnDte())); // 返回日时
		order.setRtnPickupPlaceId(getPlaceId(orderInfo.getRtnPickupLoc())); // 返回上车地点ID
		order.setRtnDropPlaceId(getPlaceId(orderInfo.getRtnDorpLoc())); // 返回终了地点ID
		order.setUseHours(orderInfo.getUseHours()); // 用车时间
		order.setOrderDistance(Integer.parseInt(orderInfo.getRouteDistance())); // 订单距离
		order.setCarCategory(orderInfo.getCarType()); // 车辆种别
		order.setPassengerName(orderInfo.getCustomerNm()); // 代表者姓名
		order.setAldultNum(orderInfo.getPassengerNum()); // 成人人数
		order.setChildNum(0); // TODO:儿童人数 未使用
		order.setBigLuggageNum(orderInfo.getLunguageNum()); // 大件行李数
		order.setSmallLuggageNum(0); // TODO:小件行李数 未使用
		order.setNeedChildSeat(orderInfo.isNeedChdChair()?"1":"0"); // 需要儿童座椅
		order.setNeedBabySeat("0"); // TODO:需要婴儿座椅 未使用
		order.setNeedAirportCheckin(orderInfo.isNeedFlightChkIn()?"1":"0"); // 需要协助值机
		order.setNeedPickupInhall(orderInfo.isNeedNameSign()?"1":"0"); // 需要举牌接机
		order.setNeedHotelCheckin(orderInfo.isNeedHotelChkIn()?"1":"0"); // 需要协助入住
		order.setServerPrice(orderInfo.getComputedPrice()); // 最终服务价格
//		order.setNeedOther(orderInfo.getCustomerId()); // 其它要求
//		order.setDriverId(orderInfo.getCustomerId()); // 司导用户ID
//		order.setCarId(orderInfo.getCustomerId()); // 分配车辆ID
		boolean ret = this.save(order);

		if (ret) {
			// 返回订单
			return order;
		} else {
			return null;
		}
	}

	private void registPlaceDistance(String fromGoogleId,String toGoogleID){
		String fromPlaceId = getPlaceId(fromGoogleId);
		String toPlaceId = getPlaceId(toGoogleID);

		String smallPlaceId = "";
		String bigPlaceId = "";
		// 地点ID比较 小->大的地点对登录
		if (Long.parseLong(fromPlaceId) < Long.parseLong(toPlaceId)) {
			smallPlaceId = fromPlaceId;
			bigPlaceId = toPlaceId;
		} else {
			smallPlaceId = fromPlaceId;
			bigPlaceId = toPlaceId;
		}

		boolean ret = true;
		ret = yyPlaceDistanceService.isPlaceIdRegisted(smallPlaceId,bigPlaceId);
		if (!ret) {
			YyPlaceDistance placeDistance = new YyPlaceDistance();
			int distance = 0;
			int duration = 0;
			RoutesResponse res = routeApi.computeRouteByPlaceId(fromGoogleId, toGoogleID);
			placeDistance.setStartPlace(smallPlaceId); // 开始地点ID
			placeDistance.setEndPlace(bigPlaceId); // 终了地点ID

			if (res.getRoutes() != null && res.getRoutes().size()>0){
				distance = Integer.parseInt(res.getRoutes().get(0).getDistanceMeters().split("m")[0]);
				duration = Integer.parseInt(res.getRoutes().get(0).getDuration().split("s")[0]);
			}
			placeDistance.setDistance(distance); // 距离：米
			placeDistance.setDuration(duration); // 时间：秒
			placeDistance.setPolyline(res.getRoutes().get(0).getPolyline().getEncodedPolyline()); // 路线：Google返回字符串
			placeDistance.setDataSource("ON_DEMAND"); // 来源
			placeDistance.setHitCount(1);
			yyPlaceDistanceService.save(placeDistance);
		}
	}
	private String getPlaceId(String googleId){
		if (StringUtil.isEmpty(googleId)){
			return "";
		}
		YyPlace yyPlace = yyPlaceMapper.selectByPlaceId(googleId);
		if (yyPlace != null){
			return yyPlace.getId();
		} else {
			yyPlace = new YyPlace();
			yyPlace.setGooglePladeId(googleId);
			List<YyPlaceTagRel> placeTagRelList = new ArrayList<>();
			String getPlaceItemsForDetail = "id,rating,userRatingCount,displayName,types,primaryType,formattedAddress,location,internationalPhoneNumber,websiteUri,reservable,rating,editorialSummary";  //photos
			try {
				placeTagRelList = googlePlacesApi.getPlaceDetails(yyPlace, 0, getPlaceItemsForDetail);
				yyPlaceService.saveMain(yyPlace, placeTagRelList);
				return yyPlace.getId();
			} catch (Exception e) {
				return "";
			}
		}
	}

	private String makeOrderNm(OrderInfoView orderInfo){
		StringBuilder sb = new StringBuilder();

		// 机场接送订单
		if ("01".equals(orderInfo.getOrderType())) {
			sb.append(orderInfo.getPickupLocNm());
			sb.append(" -- ");
			sb.append(orderInfo.getDropLocNm());
		} else if ("02".equals(orderInfo.getOrderType())) {
			// 一般接送订单
			sb.append(orderInfo.getPickupLocNm());
			sb.append(" -- ");
			sb.append(orderInfo.getDropLocNm());

			if ("4".equals(orderInfo.getOrderKbn())) {
				sb.append(" <--> ");
				sb.append(orderInfo.getRtnPickupLocNm());
				sb.append(" -- ");
				sb.append(orderInfo.getRtnDorpLocNm());
			}
		} else if ("03".equals(orderInfo.getOrderType())) {
			// 按小时包车订单
			sb.append(orderInfo.getUseHours());
			sb.append(" 小时用车");
		} else if ("04".equals(orderInfo.getOrderType())) {
			// 按行程包车订单
			sb.append(getResultByLang(yyRouteService.getById(orderInfo.getRouteId())));
		}

		return sb.toString();
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
	private Date formatterTime(String str) {
		Date date = new Date();
		if(!StringUtil.isEmpty(str)) {
			// 定义格式
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			// 1. 字符串 → LocalDateTime
			LocalDateTime localDateTime = LocalDateTime.parse(str, formatter);
			// 2. LocalDateTime → Date
			date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		}
		return date;
	}
}
