package org.jeecg.modules.yy.events;

import lombok.Data;
import org.jeecg.modules.yy.place.entity.YyOrder;

/**
 * Description:客服确认司机公司转账完成事件，引发结算完成通知邮件（09_settlement_completed.ftl）
 *
 * @Author 姜雪飛
 * @Create 2026/03/01
 * @Version 0.1
 */
@Data
public class SettlementCompletedEvent {
    private final YyOrder order;

    public SettlementCompletedEvent(YyOrder order) {
        this.order = order;
    }
}