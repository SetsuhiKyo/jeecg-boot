package org.jeecg.modules.yy.events;

import lombok.Data;
import org.jeecg.modules.yy.place.entity.YyOrder;

/**
 * Description:顾客【取消订单】事件，引发订单取消通知邮件（02_order_confirmed.ftl）
 *
 * @Author 姜雪飛
 * @Create 2026/03/01
 * @Version 0.1
 */
@Data
public class OrderCancelledEvent {
    private final YyOrder order;
    private final boolean isNeedRefund;

    public OrderCancelledEvent(YyOrder order, boolean isNeedRefund) {
        this.order = order;
        this.isNeedRefund = isNeedRefund;
    }
}