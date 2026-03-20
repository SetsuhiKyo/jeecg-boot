package org.jeecg.modules.yy.place.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.yy.place.entity.YyUserDiscount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.yy.place.vo.DiscountInfo;

/**
 * @Description: 用户优惠
 * @Author: jeecg-boot
 * @Date:   2026-01-26
 * @Version: V1.0
 */
public interface YyUserDiscountMapper extends BaseMapper<YyUserDiscount> {

    public List<DiscountInfo> getUserDiscount(@Param("customerId") String customerId);
}
