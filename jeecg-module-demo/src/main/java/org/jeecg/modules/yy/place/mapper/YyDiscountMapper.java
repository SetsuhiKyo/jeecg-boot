package org.jeecg.modules.yy.place.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.yy.place.entity.YyDiscount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 優惠情報
 * @Author: jeecg-boot
 * @Date:   2026-01-26
 * @Version: V1.0
 */
public interface YyDiscountMapper extends BaseMapper<YyDiscount> {
    /**
     * 查询当前有效的系统优惠
     *
     * @param type 优惠类别
     * @return List<YyDiscount>
     */
    public List<YyDiscount> getValidDiscount(@Param("type") String type);
}
