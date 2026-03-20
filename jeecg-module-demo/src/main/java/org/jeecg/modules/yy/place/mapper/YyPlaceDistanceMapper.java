package org.jeecg.modules.yy.place.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.yy.place.entity.YyPlaceDistance;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 距离情报
 * @Author: jeecg-boot
 * @Date:   2026-01-18
 * @Version: V1.0
 */
public interface YyPlaceDistanceMapper extends BaseMapper<YyPlaceDistance> {

    /**
     * 取得地点对结果
     *
     * @param fromPlaceId 开始地点id（小）
     * @param toPlaceId 终了地点id（大）
     * @return boolean
     */
    public int getResultByFromTo(@Param("fromPlaceId") String fromPlaceId, @Param("toPlaceId") String toPlaceId);

    /**
     * 取得地点间距离
     *
     * @param fromPlaceId 开始地点id（小）
     * @param toPlaceId 终了地点id（大）
     * @return boolean
     */
    public int getDistanceByFromTo(@Param("fromPlaceId") String fromPlaceId, @Param("toPlaceId") String toPlaceId);
}
