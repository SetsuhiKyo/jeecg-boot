package org.jeecg.modules.yy.place.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.yy.place.entity.YyUserReview;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.yy.place.vo.RouteInfoView;

/**
 * @Description: 用户评价
 * @Author: jeecg-boot
 * @Date:   2026-02-06
 * @Version: V1.0
 */
public interface YyUserReviewMapper extends BaseMapper<YyUserReview> {

    public RouteInfoView getReviewInfoByRouteId(@Param("routeId") String routeId);

}
