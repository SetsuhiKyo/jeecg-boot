package org.jeecg.modules.yy.events;

import lombok.Data;
import org.jeecg.modules.yy.place.entity.YyOrder;

/**
 * Description:提醒顾客付款事件，引发提醒顾客付款邮件（03_payment_reminder.ftl）
 *
 * @Author 姜雪飛
 * @Create 2026/03/01
 * @Version 0.1
 */
@Data
public class PaymentReminderEvent {
    private final YyOrder order;

    public PaymentReminderEvent(YyOrder order) {
        this.order = order;
    }
}