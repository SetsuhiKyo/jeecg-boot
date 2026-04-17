package org.jeecg.modules.yy.events;

import lombok.Data;
import org.jeecg.modules.yy.place.entity.YyOrder;

/**
 * Description:顾客或客服【服务完成】事件，引发结算申请邮件（08_settlement_request.ftl）
 *
 * @Author 姜雪飛
 * @Create 2026/03/01
 * @Version 0.1
 */
@Data
public class SettlementRequestEvent {
    private final YyOrder order;

    public SettlementRequestEvent(YyOrder order) {
        this.order = order;
    }
}