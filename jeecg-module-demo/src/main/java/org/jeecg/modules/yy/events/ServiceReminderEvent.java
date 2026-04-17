package org.jeecg.modules.yy.events;

import lombok.Data;
import org.jeecg.modules.yy.place.entity.YyOrder;

/**
 * Description:确认支付后【待服务】事件，引发提醒顾客服务即将开始邮件（05_service_reminder.ftl）
 *
 * @Author 姜雪飛
 * @Create 2026/03/01
 * @Version 0.1
 */
@Data
public class ServiceReminderEvent {
    private final YyOrder order;

    public ServiceReminderEvent(YyOrder order) {
        this.order = order;
    }
}