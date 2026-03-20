package org.jeecg.modules.yy.place.mapper;

import java.util.List;
import org.jeecg.modules.yy.place.entity.YyPlaceTagRel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 地点标签关系
 * @Author: jeecg-boot
 * @Date:   2025-07-01
 * @Version: V1.0
 */
public interface YyPlaceTagRelMapper extends BaseMapper<YyPlaceTagRel> {

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
   * @return List<YyPlaceTagRel>
   */
	public List<YyPlaceTagRel> selectByMainId(@Param("mainId") String mainId);
}
