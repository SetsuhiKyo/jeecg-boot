package org.jeecg.modules.yy.place.mapper;

import java.util.List;
import org.jeecg.modules.yy.place.entity.YyRouteDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 行程明细
 * @Author: jeecg-boot
 * @Date:   2025-07-25
 * @Version: V1.0
 */
public interface YyRouteDetailMapper extends BaseMapper<YyRouteDetail> {

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
    * @return List<YyRouteDetail>
    */
	public List<YyRouteDetail> selectByMainId(@Param("mainId") String mainId);


}
