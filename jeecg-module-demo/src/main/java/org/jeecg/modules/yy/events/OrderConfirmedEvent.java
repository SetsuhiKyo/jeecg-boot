package org.jeecg.modules.yy.events;

import lombok.Data;
import org.jeecg.modules.yy.place.entity.YyOrder;

/**
 * Description:订单确认（安排好司机和时间）事件，引发订单确认（等待付款）邮件（02_order_confirmed.ftl）
 *
 * @Author 姜雪飛
 * @Create 2026/03/01
 * @Version 0.1
 */
@Data
public class OrderConfirmedEvent {
    private final YyOrder order;

    public OrderConfirmedEvent(YyOrder order) {
        this.order = order;
    }
}