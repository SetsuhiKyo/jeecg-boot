package org.jeecg.modules.yy.place.service;

import org.jeecg.modules.yy.place.OrderStatus;
import org.jeecg.modules.yy.place.PaymentStatus;
import org.jeecg.modules.yy.place.entity.YyOrderReview;
import org.jeecg.modules.yy.place.entity.YyOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.yy.place.vo.OrderInfoView;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 车务订单
 * @Author: jeecg-boot
 * @Date:   2025-07-24
 * @Version: V1.0
 */
public interface IYyOrderService extends IService<YyOrder> {

	/**
	 * 使用用户ID取得订单一览
	 *
	 * @param customerId
	 */
	public List<OrderInfoView> getCustomerOrder(String customerId);

	/**
	 * 登录订单
	 */
	public String registOrderAndPlaceDistance(OrderInfoView orderInfo);

	/**
	 * 更新订单状态
	 */
//	public boolean updateOrderSts(String orderId, OrderStatus orderSts);

	/**
	 * 更新支付状态
	 */
//	public boolean updatePaymentSts(String orderId, PaymentStatus paymentSts);
	/**
	 * 后台更新订单情报
	 */
	public boolean updateByOrderInfo(YyOrder orderInfo);
	/**
	 * 前台更新订单状态
	 */
	public boolean updateByOrderStatus(String orderId,String orderSts);
	/**
	 * 前台更新支付状态
	 */
	public boolean updateByPaymentSts(String orderId,String orderSts);
}
