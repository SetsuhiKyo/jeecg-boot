package org.jeecg.modules.yy.place.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.yy.place.entity.YyActivationToken;
import org.jeecg.modules.yy.place.entity.YyCustomer;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 顾客情报
 * @Author: jeecg-boot
 * @Date:   2026-01-31
 * @Version: V1.0
 */
public interface IYyCustomerService extends IService<YyCustomer> {
    /**
     * 校验用户是否有效
     * @param yyCustomer
     * @return
     */
    public Result checkUserIsEffective(YyCustomer yyCustomer);

    /**
     * 根据用户名查询
     * @param username 用户名
     * @return LoginUser
     */
    public LoginUser getUserByName(String username);

    /**
     * 添加顾客（顾客注册）
     * @param yyCustomer 顾客信息
     */
    public boolean addCustomer(YyCustomer yyCustomer);

    /**
     * 激活顾客账户
     * @param yyCustomer 顾客信息
     * @param activationToken 激活token信息
     */
    public boolean activateCustomer(YyCustomer yyCustomer, YyActivationToken activationToken);
}
