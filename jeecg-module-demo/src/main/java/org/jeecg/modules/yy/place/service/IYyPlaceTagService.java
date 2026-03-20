package org.jeecg.modules.yy.place.service;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.yy.place.entity.YyPlaceSearchCenterCoord;
import org.jeecg.modules.yy.place.entity.YyPlaceTag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: 地点标签
 * @Author: jeecg-boot
 * @Date:   2025-07-01
 * @Version: V1.0
 */
public interface IYyPlaceTagService extends IService<YyPlaceTag> {
    public List<String> getPrimaryTypeByFlg();

//    public List<String> getTagCdByPrimaryType(String[] primaryTypes);
    public List<String> getTagCdByPrimaryType(List<String> primaryTypes);
}
