package org.jeecg.modules.yy.place.service.impl;

import dm.jdbc.util.StringUtil;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.yy.api.GoogleRoutesAPIV2;
import org.jeecg.modules.yy.api.RoutesResponse;
import org.jeecg.modules.yy.place.entity.YyPlace;
import org.jeecg.modules.yy.place.entity.YyPlaceDistance;
import org.jeecg.modules.yy.place.mapper.YyPlaceDistanceMapper;
import org.jeecg.modules.yy.place.mapper.YyPlaceMapper;
import org.jeecg.modules.yy.place.service.IYyPlaceDistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @Description: 距离情报
 * @Author: jeecg-boot
 * @Date:   2026-01-18
 * @Version: V1.0
 */
@Service
public class YyPlaceDistanceServiceImpl extends ServiceImpl<YyPlaceDistanceMapper, YyPlaceDistance> implements IYyPlaceDistanceService {

    @Autowired
    private YyPlaceDistanceMapper yyPlaceDistanceMapper;
    @Autowired
    private GoogleRoutesAPIV2 routeApi;
    @Autowired
    private YyPlaceMapper yyPlaceMapper;

    @Override
    public boolean isPlaceIdRegisted(String fromPlaceId, String toPlaceId) {
        int  ret = yyPlaceDistanceMapper.getResultByFromTo(fromPlaceId, toPlaceId);
        return ret > 0 ? true : false;
    }

    @Override
    public int getDistanceByFromTO(String fromPlaceId, String toPlaceId) {
        int distance = 0;
        String smallPlaceId = "";
        String bigPlaceId = "";

        YyPlace fromPlace = yyPlaceMapper.selectByPlaceId(fromPlaceId);
        YyPlace toPlace = yyPlaceMapper.selectByPlaceId(toPlaceId);

        if (fromPlace != null && toPlace != null){
            // 地点ID比较 小->大的地点对登录
            if (Long.parseLong(fromPlace.getId()) < Long.parseLong(toPlace.getId())) {
                smallPlaceId = fromPlace.getId();
                bigPlaceId = toPlace.getId();
            } else {
                smallPlaceId = toPlace.getId();
                bigPlaceId = fromPlace.getId();
            }

            boolean ret = true;
            ret = this.isPlaceIdRegisted(smallPlaceId, bigPlaceId);
            if (ret) {
                distance = yyPlaceDistanceMapper.getDistanceByFromTo(smallPlaceId, bigPlaceId);
            } else {
                RoutesResponse res = routeApi.computeRouteByPlaceId(fromPlaceId, toPlaceId);
                if (res.getRoutes() != null && res.getRoutes().size() > 0) {
                    distance = Integer.parseInt(res.getRoutes().get(0).getDistanceMeters().split("m")[0]);
                }
            }
        } else {
            RoutesResponse res = routeApi.computeRouteByPlaceId(fromPlaceId, toPlaceId);
            if (res.getRoutes() != null && res.getRoutes().size() > 0) {
                distance = Integer.parseInt(res.getRoutes().get(0).getDistanceMeters().split("m")[0]);
            }
        }
        return distance;
    }
}
