package org.jeecg.modules.yy.place.mapper;

import java.util.List;
import org.jeecg.modules.yy.place.entity.YyOrderReview;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 用户评价
 * @Author: jeecg-boot
 * @Date:   2025-07-24
 * @Version: V1.0
 */
public interface YyOrderReviewMapper extends BaseMapper<YyOrderReview> {

	/**
	 * 通过主表id删除子表数据
	 *
	 * @param mainId 主表id
	 * @return boolean
	 */
	public boolean deleteByMainId(@Param("mainId") String mainId);

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId 主表id
   * @return List<YyOrderReview>
   */
	public List<YyOrderReview> selectByMainId(@Param("mainId") String mainId);
}
