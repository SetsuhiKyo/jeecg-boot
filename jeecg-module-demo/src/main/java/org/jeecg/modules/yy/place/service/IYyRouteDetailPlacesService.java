package org.jeecg.modules.yy.place.service;

import org.jeecg.modules.yy.place.entity.YyRouteDetailPlaces;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 行程明细经由地点
 * @Author: jeecg-boot
 * @Date:   2025-07-29
 * @Version: V1.0
 */
public interface IYyRouteDetailPlacesService extends IService<YyRouteDetailPlaces> {
    YyRouteDetailPlaces getPrvPlace(String routeId, String detailId, Integer branchNo);

    /**
     * 通过行程id查询行程中所有地点的名称（数组）
     *
     * @param routeId
     * @return String[]
     */
    public String[] getPlacesByRouteId(String routeId);
}
