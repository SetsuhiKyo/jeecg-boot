package org.jeecg.modules.yy.place.service.impl;

import org.jeecg.modules.yy.place.entity.YyOrderReview;
import org.jeecg.modules.yy.place.mapper.YyOrderReviewMapper;
import org.jeecg.modules.yy.place.service.IYyOrderReviewService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 用户评价
 * @Author: jeecg-boot
 * @Date:   2025-07-24
 * @Version: V1.0
 */
@Service
public class YyOrderReviewServiceImpl extends ServiceImpl<YyOrderReviewMapper, YyOrderReview> implements IYyOrderReviewService {
	
	@Autowired
	private YyOrderReviewMapper yyOrderReviewMapper;
	
	@Override
	public List<YyOrderReview> selectByMainId(String mainId) {
		return yyOrderReviewMapper.selectByMainId(mainId);
	}
}
