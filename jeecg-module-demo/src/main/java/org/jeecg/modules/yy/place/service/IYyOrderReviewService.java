package org.jeecg.modules.yy.place.service;

import org.jeecg.modules.yy.place.entity.YyOrderReview;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 用户评价
 * @Author: jeecg-boot
 * @Date:   2025-07-24
 * @Version: V1.0
 */
public interface IYyOrderReviewService extends IService<YyOrderReview> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<YyOrderReview>
	 */
	public List<YyOrderReview> selectByMainId(String mainId);
}
