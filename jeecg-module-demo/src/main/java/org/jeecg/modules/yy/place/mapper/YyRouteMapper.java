package org.jeecg.modules.yy.place.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.yy.place.entity.YyRoute;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.yy.place.entity.YyRoutePrice;

/**
 * @Description: 行程情报
 * @Author: jeecg-boot
 * @Date:   2025-07-25
 * @Version: V1.0
 */
public interface YyRouteMapper extends BaseMapper<YyRoute> {

    /**
     * 查询当前流行行程
     *
     * @param recommendFlg 当前推荐对象
     * @return List<YyRoute>
     */
    public List<YyRoute> selectByRecommendFlg(@Param("recommendFlg") String recommendFlg,@Param("customerId") String customerId);

}
