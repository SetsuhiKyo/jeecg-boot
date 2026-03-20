package org.jeecg.modules.yy.place.service;

import org.jeecg.modules.yy.place.entity.YyRoutePrice;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 行程价格
 * @Author: jeecg-boot
 * @Date:   2025-07-25
 * @Version: V1.0
 */
public interface IYyRoutePriceService extends IService<YyRoutePrice> {

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId
   * @return List<YyRoutePrice>
   */
	public List<YyRoutePrice> selectByMainId(String mainId);
}
