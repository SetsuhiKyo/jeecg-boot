package org.jeecg.modules.yy.place.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.yy.place.entity.YyRoute;
import org.jeecg.modules.yy.place.entity.YyRouteDetailPlaces;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.yy.place.vo.YyRoutesDtailDayPlace;

/**
 * @Description: 行程明细经由地点
 * @Author: jeecg-boot
 * @Date:   2025-07-29
 * @Version: V1.0
 */
public interface YyRouteDetailPlacesMapper extends BaseMapper<YyRouteDetailPlaces> {
    /**
     * 取得同一个路程明细的前一个地点ID
     *
     * @param routeId 路程id
     * @param detailId 路程明细id
     * @param branchNo 路程明细地点支番
     * @return 前一个地点ID
     */
    public YyRouteDetailPlaces getPrvPlace(@Param("routeId") String routeId,@Param("detailId") String detailId,@Param("branchNo") Integer branchNo);

    /**
     * 根据行程ID取得该行程下所有的地点
     *
     * @param routeId 路程id
     * @return List<YyRouteDetailPlaces>
     */
    public List<YyRouteDetailPlaces> selectByRouteId(@Param("routeId") String routeId);

    /**
     * 行程检索，分页取得
     *
     * @param placeIds 路程id（复数可。空的情况取得所有行程）
     * @param offset 位移量（开始取得行程的Index）
     * @param pageSize 每页的取得条数
     * @param placeCount 路程id的个数（复数可。空的情况取得所有行程）
     * @return List<YyRouteDetailPlaces>
     */
    public List<YyRoute> selectRouteByPlacesPage(@Param("placeIds") List<String> placeIds,@Param("offset") int offset,@Param("pageSize") int pageSize,@Param("placeCount") int placeCount,@Param("customerId") String customerId);

    /**
     * 行程检索，分页取得
     *
     * @param routeId 行程id
     * @param detailId 行程详细id
     * @return List<YyRouteDetailPlaces>
     */
    public List<YyRoutesDtailDayPlace> getDetailPlaces(@Param("routeId") String routeId, @Param("detailId") String detailId);
}
