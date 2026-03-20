package org.jeecg.modules.yy.place.mapper;

import java.math.BigDecimal;
import java.util.List;
import org.jeecg.modules.yy.place.entity.YyRoutePrice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 行程价格
 * @Author: jeecg-boot
 * @Date:   2025-07-25
 * @Version: V1.0
 */
public interface YyRoutePriceMapper extends BaseMapper<YyRoutePrice> {

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
    * @return List<YyRoutePrice>
    */
	public List<YyRoutePrice> selectByMainId(@Param("mainId") String mainId);

	/**
	 * 通过行程id和车辆类型查询行程价格
	 *
	 * @param carType 车辆类型
	 * @param routeId 行程id
	 * @return YyRoutePrice
	 */
	public BigDecimal getRouteBasicPrice(@Param("carType") String carType, @Param("routeId") String routeId);

}
