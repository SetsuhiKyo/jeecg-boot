package org.jeecg.modules.yy.place.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.yy.place.entity.YyPlace;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 地点情报
 * @Author: jeecg-boot
 * @Date:   2025-07-01
 * @Version: V1.0
 */
public interface YyPlaceMapper extends BaseMapper<YyPlace> {
    public int getCountByPlaceId(@Param("placeId") String placeId) ;
    public YyPlace selectByPlaceId(@Param("placeId") String placeId) ;
    public List<YyPlace> getPlacesByInput(@Param("input") String input) ;

    public List<YyPlace> getAirports();
}
