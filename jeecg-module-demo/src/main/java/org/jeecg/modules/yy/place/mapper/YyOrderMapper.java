package org.jeecg.modules.yy.place.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.yy.place.entity.YyOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.yy.place.entity.YyRoute;
import org.jeecg.modules.yy.place.vo.OrderInfoView;

/**
 * @Description: 车务订单
 * @Author: jeecg-boot
 * @Date:   2025-07-24
 * @Version: V1.0
 */
public interface YyOrderMapper extends BaseMapper<YyOrder> {
    /**
     * 所有订单里，综合TOP行程
     *
     *
     * @param days 最近多少天内的订单作为对象  默认：30天
     * @param topNums TOP多少条记录取得  默认：5条
     * @return List<YyRoute>
     */
    public List<YyRoute> selectTopOrderRoutes(@Param("days") int days, @Param("topNums") int topNums,@Param("customerId") String customerId);

    /**
     * 使用用户ID取得订单一览
     *
     * @param customerId
     */
    public List<OrderInfoView> getCustomerOrder(@Param("customerId") String customerId);
}
