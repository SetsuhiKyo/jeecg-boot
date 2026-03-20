package org.jeecg.modules.yy.place.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import dm.jdbc.util.StringUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.desensitization.annotation.SensitiveEncode;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.yy.events.CustomerAddedEvent;
import org.jeecg.modules.yy.place.entity.YyActivationToken;
import org.jeecg.modules.yy.place.entity.YyCustomer;
import org.jeecg.modules.yy.place.mapper.YyCustomerMapper;
import org.jeecg.modules.yy.place.service.IYyActivationTokenService;
import org.jeecg.modules.yy.place.service.IYyCustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * @Description: 顾客情报
 * @Author: jeecg-boot
 * @Date:   2026-01-31
 * @Version: V1.0
 */
@Service
public class YyCustomerServiceImpl extends ServiceImpl<YyCustomerMapper, YyCustomer> implements IYyCustomerService {

    @Autowired
    private BaseCommonService baseCommonService;
    @Autowired
    private IYyActivationTokenService yyActivationTokenService;
    @Autowired
    private YyCustomerMapper yyCustomerMapper;
    @Autowired
    private ApplicationEventPublisher publisher;
    /**
     * 校验用户是否有效
     * @param yyCustomer
     * @return
     */
    @Override
    public Result<?> checkUserIsEffective(YyCustomer yyCustomer) {
        Result<?> result = new Result<Object>();
        YyActivationToken yyActivationToken = new YyActivationToken();

        //情况1：根据用户信息查询，该用户未激活
        if (CommonConstant.CUSTOMER_DISACTIVE.equals(yyCustomer.getCustormerSts())) {
            // 重新发送激活邮件
            yyActivationTokenService.reSendVerifyMail(yyCustomer.getId());
            baseCommonService.addLog("顾客登录失败，邮件:" + yyCustomer.getEmail() + "的顾客未激活！", CommonConstant.LOG_TYPE_1, null);
            result.error500("通过该邮件注册的顾客未激活，我们已向您的邮箱发送了激活链接，请查收！");
            return result;
        }
        //情况2：根据用户信息查询，该用户已激活
        if (CommonConstant.CUSTOMER_ACTIVED.equals(yyCustomer.getCustormerSts())) {
            baseCommonService.addLog("顾客登录失败，邮件:" + yyCustomer.getEmail() + "的顾客已存在！", CommonConstant.LOG_TYPE_1, null);
            result.error500("该邮件已经注册成功！如果您忘记了用户名和密码，请联系我们的客服人员。");
            return result;
        }
        //情况3：根据用户信息查询，该用户已冻结
        if (CommonConstant.CUSTOMER_FREEZE.equals(yyCustomer.getCustormerSts())) {
            baseCommonService.addLog("顾客登录失败，邮件:" + yyCustomer.getEmail() + "的顾客已冻结！", CommonConstant.LOG_TYPE_1, null);
            result.error500("该邮件已注册，请使用别的邮箱重新注册！");
            return result;
        }
        return result;
    }

    /**
     * 根据用户名查询
     * @param username 用户名
     * @return LoginUser
     */
    @Override
//    @Cacheable(cacheNames= CacheConstant.SYS_USERS_CACHE, key="#username")
    @SensitiveEncode
    public LoginUser getUserByName(String username){
        if(oConvertUtils.isEmpty(username)) {
            return null;
        }
        LoginUser loginUser = new LoginUser();
        YyCustomer yyCustomer = yyCustomerMapper.getUserByName(username);

        if(yyCustomer==null) {
            return null;
        }

        loginUser.setId(yyCustomer.getId());
        loginUser.setUsername(yyCustomer.getCustomerName());
        loginUser.setRealname(yyCustomer.getNickName());
        loginUser.setPassword(yyCustomer.getCustomerPwd());
        loginUser.setRoleCode("customer");
        loginUser.setAvatar(""); //TODO:保留
        loginUser.setEmail(yyCustomer.getEmail());
        loginUser.setPhone(yyCustomer.getPhoneNo());
        loginUser.setStatus(Integer.parseInt(yyCustomer.getCustormerSts()));

        return loginUser;
    }

    /**
     * 添加顾客（顾客注册）
     * @param yyCustomer 顾客信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addCustomer(YyCustomer yyCustomer){
        // 顾客没有输入账号，邮箱作为账号
        String account = "";
        if (StringUtil.isEmpty(yyCustomer.getCustomerName())){
            account = yyCustomer.getEmail();
        } else {
            account = yyCustomer.getCustomerName();
        }
        // 顾客没有输入密码，邮箱作为初始密码
        String password = "";
        if (StringUtil.isEmpty(yyCustomer.getCustomerPwd())){
            password = yyCustomer.getEmail();
        } else {
            password = yyCustomer.getCustomerPwd();
        }
        // 向顾客表中插入一条记录
        yyCustomer.setCustomerName(account);
        String salt = oConvertUtils.randomGen(8);
        yyCustomer.setCustomerSalt(salt);
        String passwordEncode = PasswordUtil.encrypt(account, password, salt);
        yyCustomer.setCustomerPwd(passwordEncode);
        yyCustomer.setCustormerSts("0"); // 0:未激活 1:已激活 2:冻结
        yyCustomer.setEmailVerify("0"); // 0:邮件未检证  1:邮件已检证
        boolean retCustomer = this.save(yyCustomer);

        // 生成激活token
        String token = UUID.randomUUID().toString().replace("-", "");

        // 向账户激活表中添加记录（用以邮件检证以及激活账户）
        YyActivationToken yyActivationToken = new YyActivationToken();
        yyActivationToken.setCustomerId(yyCustomer.getId());
        yyActivationToken.setToken(token); // 令牌
        yyActivationToken.setTokenExpireat(LocalDateTime.now().plusHours(24)); // 令牌有效期：24小时
        yyActivationToken.setUsed("0"); // 未使用
        boolean ret = yyActivationTokenService.save(yyActivationToken);

        // 向顾客发送验证邮件
        if (retCustomer && ret) {
            publisher.publishEvent(new CustomerAddedEvent(yyCustomer, token));
        }

        return retCustomer && ret;
    }

    /**
     * 激活顾客账户
     * @param yyCustomer 顾客信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean activateCustomer(YyCustomer yyCustomer,YyActivationToken activationToken){
        // 顾客表里的状态更新
        yyCustomer.setCustormerSts(CommonConstant.CUSTOMER_ACTIVED); // 顾客账户已激活
        yyCustomer.setEmailVerify(CommonConstant.STATUS_1); // 邮件已检证
        boolean upCustomer = this.saveOrUpdate(yyCustomer);

        // 账户激活表里的旧记录更新成【已使用】
        activationToken.setUsed(CommonConstant.STATUS_1); // Token已使用
        boolean upTokenSts = yyActivationTokenService.saveOrUpdate(activationToken);

        if (upCustomer && upTokenSts) {
            baseCommonService.addLog("顾客激活，username： " + yyCustomer.getCustomerName(), CommonConstant.LOG_TYPE_2, 2);
        }
        return upCustomer && upTokenSts;
    }

}
