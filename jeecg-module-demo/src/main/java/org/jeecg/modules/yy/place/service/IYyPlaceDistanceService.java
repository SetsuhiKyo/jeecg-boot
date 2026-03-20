package org.jeecg.modules.yy.place.service;

import org.jeecg.modules.yy.place.entity.YyPlaceDistance;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 距离情报
 * @Author: jeecg-boot
 * @Date:   2026-01-18
 * @Version: V1.0
 */
public interface IYyPlaceDistanceService extends IService<YyPlaceDistance> {

    public boolean isPlaceIdRegisted(String fromPlaceId,String toPlaceId);

    public int getDistanceByFromTO(String fromPlaceId,String toPlaceId);
}
