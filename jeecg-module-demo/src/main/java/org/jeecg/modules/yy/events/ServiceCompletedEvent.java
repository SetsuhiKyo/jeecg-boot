package org.jeecg.modules.yy.events;

import lombok.Data;
import org.jeecg.modules.yy.place.entity.YyOrder;

/**
 * Description:顾客确认【服务完成】事件，引发服务完成通知邮件（06_service_completed.ftl）
 *
 * @Author 姜雪飛
 * @Create 2026/03/01
 * @Version 0.1
 */
@Data
public class ServiceCompletedEvent {
    private final YyOrder order;

    public ServiceCompletedEvent(YyOrder order) {
        this.order = order;
    }
}