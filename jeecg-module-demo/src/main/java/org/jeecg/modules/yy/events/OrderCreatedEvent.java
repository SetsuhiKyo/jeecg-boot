package org.jeecg.modules.yy.events;

import lombok.Data;
import org.jeecg.modules.yy.place.entity.YyOrder;

/**
 * Description:订单预约成功事件，引发订单预约成功邮件（01_order_created.ftl）
 *
 * @Author 姜雪飛
 * @Create 2026/03/01
 * @Version 0.1
 */
@Data
public class OrderCreatedEvent {
    private final YyOrder order;

    public OrderCreatedEvent(YyOrder order) {
        this.order = order;
    }
}