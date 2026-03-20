package org.jeecg.modules.yy.place.service.impl;

import org.jeecg.modules.yy.place.entity.YyPlaceTagRel;
import org.jeecg.modules.yy.place.mapper.YyPlaceTagRelMapper;
import org.jeecg.modules.yy.place.service.IYyPlaceTagRelService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @Description: 地点标签关系
 * @Author: jeecg-boot
 * @Date:   2025-07-01
 * @Version: V1.0
 */
@Service
public class YyPlaceTagRelServiceImpl extends ServiceImpl<YyPlaceTagRelMapper, YyPlaceTagRel> implements IYyPlaceTagRelService {

	@Resource
	private YyPlaceTagRelMapper yyPlaceTagRelMapper;
	
	@Override
	public List<YyPlaceTagRel> selectByMainId(String mainId) {
		return yyPlaceTagRelMapper.selectByMainId(mainId);
	}
}
