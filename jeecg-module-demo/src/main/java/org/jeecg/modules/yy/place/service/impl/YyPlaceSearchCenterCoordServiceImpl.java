package org.jeecg.modules.yy.place.service.impl;

import org.jeecg.modules.yy.place.entity.YyPlaceSearchCenterCoord;
import org.jeecg.modules.yy.place.mapper.YyPlaceSearchCenterCoordMapper;
import org.jeecg.modules.yy.place.service.IYyPlaceSearchCenterCoordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 地点采集中心坐标
 * @Author: jeecg-boot
 * @Date:   2025-07-01
 * @Version: V1.0
 */
@Service
public class YyPlaceSearchCenterCoordServiceImpl extends ServiceImpl<YyPlaceSearchCenterCoordMapper, YyPlaceSearchCenterCoord> implements IYyPlaceSearchCenterCoordService {

    @Resource
    private YyPlaceSearchCenterCoordMapper yyPlaceSearchCenterCoordMapper;
    @Override
    public List<YyPlaceSearchCenterCoord> getSearchCentersByFlg(){
        return yyPlaceSearchCenterCoordMapper.getSearchCentersByFlg();
    }
}
