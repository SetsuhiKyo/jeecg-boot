package org.jeecg.modules.yy.place.service.impl;

import org.jeecg.modules.yy.place.entity.YyRoutePrice;
import org.jeecg.modules.yy.place.mapper.YyRoutePriceMapper;
import org.jeecg.modules.yy.place.service.IYyRoutePriceService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 行程价格
 * @Author: jeecg-boot
 * @Date:   2025-07-25
 * @Version: V1.0
 */
@Service
public class YyRoutePriceServiceImpl extends ServiceImpl<YyRoutePriceMapper, YyRoutePrice> implements IYyRoutePriceService {
	
	@Autowired
	private YyRoutePriceMapper yyRoutePriceMapper;
	
	@Override
	public List<YyRoutePrice> selectByMainId(String mainId) {
		return yyRoutePriceMapper.selectByMainId(mainId);
	}
}
