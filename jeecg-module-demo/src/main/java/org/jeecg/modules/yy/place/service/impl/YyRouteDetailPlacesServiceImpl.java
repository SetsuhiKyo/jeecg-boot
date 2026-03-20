package org.jeecg.modules.yy.place.service.impl;

import org.jeecg.modules.yy.place.entity.YyRouteDetail;
import org.jeecg.modules.yy.place.entity.YyRouteDetailPlaces;
import org.jeecg.modules.yy.place.mapper.YyRouteDetailMapper;
import org.jeecg.modules.yy.place.mapper.YyRouteDetailPlacesMapper;
import org.jeecg.modules.yy.place.service.IYyRouteDetailPlacesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 行程明细经由地点
 * @Author: jeecg-boot
 * @Date:   2025-07-29
 * @Version: V1.0
 */
@Service
public class YyRouteDetailPlacesServiceImpl extends ServiceImpl<YyRouteDetailPlacesMapper, YyRouteDetailPlaces> implements IYyRouteDetailPlacesService {
    @Autowired
    private YyRouteDetailPlacesMapper yyRouteDetailPlacesMapper;

    @Override
    public YyRouteDetailPlaces getPrvPlace(String routeId, String detailId, Integer branchNo) {
        return yyRouteDetailPlacesMapper.getPrvPlace(routeId,detailId,branchNo);
    }

    @Override
    public String[] getPlacesByRouteId(String routeId) {
        List<String> palces = new ArrayList<>();
        List<YyRouteDetailPlaces> routePlaces = yyRouteDetailPlacesMapper.selectByRouteId(routeId);
        if (routePlaces != null && routePlaces.size() > 0){
            for(YyRouteDetailPlaces routeDel : routePlaces) {
                if (1< routeDel.getBranchNo() && routeDel.getBranchNo() < routePlaces.size()) {
                    // 接送酒店不作为景点表示
                    palces.add(routeDel.getPassPlaceNm());
                }
            }
        }

        return palces.toArray(new String[0]);
    }
}
