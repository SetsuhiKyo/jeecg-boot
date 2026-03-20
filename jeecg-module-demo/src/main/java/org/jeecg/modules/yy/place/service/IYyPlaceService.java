package org.jeecg.modules.yy.place.service;

import org.jeecg.modules.yy.place.entity.YyPlaceTagRel;
import org.jeecg.modules.yy.place.entity.YyPlace;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.yy.place.vo.AutoCompleteText;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 地点情报
 * @Author: jeecg-boot
 * @Date:   2025-07-01
 * @Version: V1.0
 */
public interface IYyPlaceService extends IService<YyPlace> {

	/**
	 * 添加一对多
	 *
	 * @param yyPlace
	 * @param yyPlaceTagRelList
	 */
	public void saveMain(YyPlace yyPlace,List<YyPlaceTagRel> yyPlaceTagRelList) ;
	
	/**
	 * 修改一对多
	 *
   * @param yyPlace
   * @param yyPlaceTagRelList
	 */
	public void updateMain(YyPlace yyPlace,List<YyPlaceTagRel> yyPlaceTagRelList);
	
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
	 * 通過地點ID查詢是否存在
	 *
	 * @param placeId
	 */
	public int getCountByPlaceId(String placeId) ;

	/**
	 * 通過地點ID查詢地点信息
	 *
	 * @param placeId
	 */
	public YyPlace selectByPlaceId(String placeId) ;

	/**
	 * 景点入力补完
	 *
	 * @param text
	 * @return
	 */
	public List<AutoCompleteText> inputAutocomplete(String text);

	/**
	 * 机场一览取得
	 *
	 * @return
	 */
	public List<AutoCompleteText> fetchAirports();
}
