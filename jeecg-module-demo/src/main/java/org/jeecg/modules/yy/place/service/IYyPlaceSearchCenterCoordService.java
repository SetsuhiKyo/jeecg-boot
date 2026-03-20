package org.jeecg.modules.yy.place.service;

import org.jeecg.modules.yy.place.entity.YyPlaceSearchCenterCoord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 地点采集中心坐标
 * @Author: jeecg-boot
 * @Date:   2025-07-01
 * @Version: V1.0
 */
public interface IYyPlaceSearchCenterCoordService extends IService<YyPlaceSearchCenterCoord> {

    public List<YyPlaceSearchCenterCoord> getSearchCentersByFlg();
}
