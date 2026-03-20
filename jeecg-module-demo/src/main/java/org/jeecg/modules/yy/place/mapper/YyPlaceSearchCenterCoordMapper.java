package org.jeecg.modules.yy.place.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.yy.place.entity.YyPlaceSearchCenterCoord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 地点采集中心坐标
 * @Author: jeecg-boot
 * @Date:   2025-07-01
 * @Version: V1.0
 */
public interface YyPlaceSearchCenterCoordMapper extends BaseMapper<YyPlaceSearchCenterCoord> {

    /**
     * 通过標記FLG查询地点采集中心坐标
     * @return
     */
    public List<YyPlaceSearchCenterCoord> getSearchCentersByFlg();
}
