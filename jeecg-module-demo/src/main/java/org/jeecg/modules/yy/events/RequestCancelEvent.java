package org.jeecg.modules.yy.events;

import lombok.Data;
import org.jeecg.modules.yy.place.entity.YyOrder;

/**
 * Description:
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