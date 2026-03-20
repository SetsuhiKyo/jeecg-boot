package org.jeecg.modules.yy.place.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.yy.place.entity.YyRoute;
import org.jeecg.modules.yy.place.entity.YyUserFavorite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 用户收藏
 * @Author: jeecg-boot
 * @Date:   2026-02-06
 * @Version: V1.0
 */
public interface YyUserFavoriteMapper extends BaseMapper<YyUserFavorite> {
    /**
     * 所有收藏里，TOP行程
     *
     * @param days 最近多少天内的订单作为对象
     * @param topNums TOP多少条记录取得
     * @return List<YyRoute>
     */
    public List<YyRoute> selectTopFavoriteRoutes(@Param("days") int days, @Param("topNums") int topNums,@Param("customerId") String customerId);
    /**
     * 我的收藏行程
     *
     * @param customerId 顾客ID
     * @return List<YyRoute>
     */
    public List<YyRoute> selectMyFavoriteRoutes(@Param("customerId") String customerId);

    /**
     * 根据用户ID和搜藏对象ID查询记录
     *
     * @return YyUserFavorite
     */
    public YyUserFavorite getResultByUserIdAndTargetId(@Param("userId") String userId,@Param("targetId") String targetId);
}
