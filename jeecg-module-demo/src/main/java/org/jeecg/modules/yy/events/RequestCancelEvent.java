package org.jeecg.modules.yy.events;

import lombok.Data;
import org.jeecg.modules.yy.place.entity.YyOrder;

/**
 * Description:在【支付确认】后取消订单事件，引发订单取消申请邮件（11_request_cancel.ftl）
 *
 * @Author 姜雪飛
 * @Create 2026/03/01
 * @Version 0.1
 */
@Data
public class RequestCancelEvent {
    private final YyOrder order;

    public RequestCancelEvent(YyOrder order) {
        this.order = order;
    }
}