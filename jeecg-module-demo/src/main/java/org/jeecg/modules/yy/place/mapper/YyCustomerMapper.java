package org.jeecg.modules.yy.place.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.yy.place.entity.YyCustomer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 顾客情报
 * @Author: jeecg-boot
 * @Date:   2026-01-31
 * @Version: V1.0
 */
public interface YyCustomerMapper extends BaseMapper<YyCustomer> {
    /**
     * 通过用户账号查询用户信息
     * @param username
     * @return
     */
    public YyCustomer getUserByName(@Param("username") String username);
}
