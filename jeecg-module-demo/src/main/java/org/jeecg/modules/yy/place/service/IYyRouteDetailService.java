package org.jeecg.modules.yy.place.service;

import org.jeecg.modules.yy.place.entity.YyRouteDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.yy.place.vo.YyRoutesDtail;

import java.util.List;

/**
 * @Description: 行程明细
 * @Author: jeecg-boot
 * @Date:   2025-07-25
 * @Version: V1.0
 */
public interface IYyRouteDetailService extends IService<YyRouteDetail> {

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId
   * @return List<YyRouteDetail>
   */
	public List<YyRouteDetail> selectByMainId(String mainId);

    /**
     * 通过行程id查询行程详细情报
     *
     * @param routeId
     * @return YyRoutesDtail
     */
    public YyRoutesDtail getRouteDtailInfo(String routeId);
}
