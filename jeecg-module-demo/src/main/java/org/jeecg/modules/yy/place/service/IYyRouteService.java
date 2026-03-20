package org.jeecg.modules.yy.place.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.yy.place.entity.YyRouteDetail;
import org.jeecg.modules.yy.place.entity.YyRoutePrice;
import org.jeecg.modules.yy.place.entity.YyRoute;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.yy.place.vo.RouteInfoView;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 行程情报
 * @Author: jeecg-boot
 * @Date:   2025-07-25
 * @Version: V1.0
 */
public interface IYyRouteService extends IService<YyRoute> {

	/**
	 * 删除一对多
	 *
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);

	/**
	 * 各种行程一览取得
	 * 1.当前流行 -- 按季节推荐，后台可选择
	 * 2.综合热门 -- 按总订单量排序
	 * 3.国家热门 -- 按国家订单量排序 (国家代码从RequestHeader里取得)
	 *
	 * @param routeKbn
	 * @param routeNum
	 */
	public List<RouteInfoView> getRoutes(String routeKbn, int routeNum, String customerId);

	/**
	 * 行程检索分页取得
	 *
	 * @param placeIds
	 * @param pageNo
	 * @param pageSize
	 */
	List<RouteInfoView> fetchRoutesPage(String[] placeIds, int pageNo, int pageSize,String customerId);
}
