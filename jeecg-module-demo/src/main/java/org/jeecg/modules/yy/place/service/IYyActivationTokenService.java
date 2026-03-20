package org.jeecg.modules.yy.place.service;

import org.jeecg.modules.yy.place.entity.YyActivationToken;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.yy.place.entity.YyCustomer;

/**
 * @Description: 账户激活
 * @Author: jeecg-boot
 * @Date:   2026-01-31
 * @Version: V1.0
 */
public interface IYyActivationTokenService extends IService<YyActivationToken> {

    public void reSendVerifyMail(String customerId);
}
