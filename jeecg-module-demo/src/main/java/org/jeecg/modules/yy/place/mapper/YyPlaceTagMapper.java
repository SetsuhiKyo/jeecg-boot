package org.jeecg.modules.yy.place.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.yy.place.entity.YyPlaceSearchCenterCoord;
import org.jeecg.modules.yy.place.entity.YyPlaceTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 地点标签
 * @Author: jeecg-boot
 * @Date:   2025-07-01
 * @Version: V1.0
 */
public interface YyPlaceTagMapper extends BaseMapper<YyPlaceTag> {
    /**
     * 通过標記FLG查询地点采集中心坐标
     * @return
     */
    public List<String> getPrimaryTypeByFlg();

    public List<String> getTagCdByPrimaryType(@Param("primaryTypes") List<String> primaryTypes);
}
