package org.jeecg.modules.yy.events;

import lombok.Data;
import org.jeecg.modules.yy.place.entity.YyOrder;

/**
 * Description:顾客【我已付款】事件，引发顾客已付款确认邮件（04_payment_paied_confirm.ftl）
 *
 *
 * @Author 姜雪飛
 * @Create 2026/03/01
 * @Version 0.1
 */
@Data
public class PaymentPaiedConfirmEvent {
    private final YyOrder order;

    public PaymentPaiedConfirmEvent(YyOrder order) {
        this.order = order;
    }
}