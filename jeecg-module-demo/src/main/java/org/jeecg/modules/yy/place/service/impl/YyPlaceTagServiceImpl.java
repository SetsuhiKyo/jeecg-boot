package org.jeecg.modules.yy.place.service.impl;

import org.jeecg.modules.yy.place.entity.YyPlaceTag;
import org.jeecg.modules.yy.place.mapper.YyPlaceSearchCenterCoordMapper;
import org.jeecg.modules.yy.place.mapper.YyPlaceTagMapper;
import org.jeecg.modules.yy.place.service.IYyPlaceTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 地点标签
 * @Author: jeecg-boot
 * @Date:   2025-07-01
 * @Version: V1.0
 */
@Service
public class YyPlaceTagServiceImpl extends ServiceImpl<YyPlaceTagMapper, YyPlaceTag> implements IYyPlaceTagService {

    @Resource
    private YyPlaceTagMapper yyPlaceTagMapper;
    @Override
    public List<String> getPrimaryTypeByFlg() {
        return yyPlaceTagMapper.getPrimaryTypeByFlg();
    }

    @Override
    public List<String> getTagCdByPrimaryType(List<String> primaryTypes) {
        return yyPlaceTagMapper.getTagCdByPrimaryType(primaryTypes);
    }

//    @Override
//    public List<String> getTagCdByPrimaryType(String[] primaryTypes) {
//        List<String>  ret = new ArrayList<>();
//        for (String type:primaryTypes){
//            String tag = yyPlaceTagMapper.getTagCdByPrimaryType(type);
//            if (tag != null && !tag.isEmpty()){
//                ret.add(tag);
//            }
//        }
//
//        return ret;
//    }
}
