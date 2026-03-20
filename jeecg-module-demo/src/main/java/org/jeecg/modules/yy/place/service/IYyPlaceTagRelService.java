package org.jeecg.modules.yy.place.service;

import org.jeecg.modules.yy.place.entity.YyPlaceTagRel;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 地点标签关系
 * @Author: jeecg-boot
 * @Date:   2025-07-01
 * @Version: V1.0
 */
public interface IYyPlaceTagRelService extends IService<YyPlaceTagRel> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<YyPlaceTagRel>
	 */
	public List<YyPlaceTagRel> selectByMainId(String mainId);
}
