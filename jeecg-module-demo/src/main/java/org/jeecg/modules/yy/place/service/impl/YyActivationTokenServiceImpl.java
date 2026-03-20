package org.jeecg.modules.yy.place.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.yy.events.CustomerAddedEvent;
import org.jeecg.modules.yy.place.entity.YyActivationToken;
import org.jeecg.modules.yy.place.entity.YyCustomer;
import org.jeecg.modules.yy.place.mapper.YyActivationTokenMapper;
import org.jeecg.modules.yy.place.mapper.YyCustomerMapper;
import org.jeecg.modules.yy.place.service.IYyActivationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @Description: 账户激活
 * @Author: jeecg-boot
 * @Date:   2026-01-31
 * @Version: V1.0
 */
@Service
public class YyActivationTokenServiceImpl extends ServiceImpl<YyActivationTokenMapper, YyActivationToken> implements IYyActivationTokenService {
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private YyCustomerMapper yyCustomerMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reSendVerifyMail(String customerId) {

        // 账户激活表里的旧记录更新成【已使用】
        LambdaUpdateWrapper<YyActivationToken> updateWrapper = new UpdateWrapper().lambda();
        updateWrapper.eq(YyActivationToken::getCustomerId, customerId);
        updateWrapper.set(YyActivationToken::getUsed, CommonConstant.STATUS_1); // 已使用
        boolean upOldToken = this.update(updateWrapper);

        // 账户激活表里追加新记录
        // 生成激活token
        String token = UUID.randomUUID().toString().replace("-", "");
        YyActivationToken yyActivationToken = new YyActivationToken();
        yyActivationToken.setCustomerId(customerId);
        yyActivationToken.setToken(token); // 令牌
        yyActivationToken.setTokenExpireat(LocalDateTime.now().plusHours(24)); // 令牌有效期：24小时
        yyActivationToken.setUsed(CommonConstant.STATUS_0); // 未使用
        boolean addNewToken =this.save(yyActivationToken);

        // 向顾客发送验证邮件
        if (upOldToken && addNewToken) {
            publisher.publishEvent(new CustomerAddedEvent(yyCustomerMapper.selectById(customerId), token));
        }
    }
}
