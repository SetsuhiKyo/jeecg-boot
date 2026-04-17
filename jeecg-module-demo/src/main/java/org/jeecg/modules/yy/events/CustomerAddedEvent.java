package org.jeecg.modules.yy.events;

import lombok.Data;
import org.jeecg.modules.yy.place.entity.YyCustomer;
import org.jeecg.modules.yy.place.entity.YyOrder;

/**
 * Description:顾客注册成功事件，引发顾客账户激活邮件（customer_account_activate.ftl）
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