package org.jeecg.modules.yy.events;

import lombok.Data;
import org.jeecg.modules.yy.place.entity.YyCustomer;
import org.jeecg.modules.yy.place.entity.YyOrder;

/**
 * Description:
 *
 * @Author 姜雪飛
 * @Create 2026/03/01
 * @Version 0.1
 */
@Data
public class CustomerAddedEvent {
    private final YyCustomer customer;
    private final String token;

    public CustomerAddedEvent(YyCustomer customer, String token) {
        this.customer = customer;
        this.token = token;
    }
}