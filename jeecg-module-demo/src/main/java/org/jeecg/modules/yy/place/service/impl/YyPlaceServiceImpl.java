package org.jeecg.modules.yy.place.service.impl;

import org.jeecg.config.yyi18n.LangContext;
import org.jeecg.modules.yy.api.GooglePlacesAPIV1;
import org.jeecg.modules.yy.place.entity.YyPlace;
import org.jeecg.modules.yy.place.entity.YyPlaceTagRel;
import org.jeecg.modules.yy.place.entity.YyRoute;
import org.jeecg.modules.yy.place.mapper.YyPlaceTagRelMapper;
import org.jeecg.modules.yy.place.mapper.YyPlaceMapper;
import org.jeecg.modules.yy.place.service.IYyPlaceService;
import org.jeecg.modules.yy.place.vo.AutoCompleteText;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;

/**
 * @Description: 地点情报
 * @Author: jeecg-boot
 * @Date:   2025-07-01
 * @Version: V1.0
 */
@Service
public class YyPlaceServiceImpl extends ServiceImpl<YyPlaceMapper, YyPlace> implements IYyPlaceService {

	@Resource
	private YyPlaceMapper yyPlaceMapper;
	@Resource
	private YyPlaceTagRelMapper yyPlaceTagRelMapper;
	@Autowired
	private GooglePlacesAPIV1 googlePlacesApi;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(YyPlace yyPlace, List<YyPlaceTagRel> yyPlaceTagRelList) {
		yyPlaceMapper.insert(yyPlace);
		if(yyPlaceTagRelList!=null && yyPlaceTagRelList.size()>0) {
			for(YyPlaceTagRel entity:yyPlaceTagRelList) {
				//外键设置
				entity.setPlaceId(yyPlace.getId());
				yyPlaceTagRelMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(YyPlace yyPlace,List<YyPlaceTagRel> yyPlaceTagRelList) {
		yyPlaceMapper.updateById(yyPlace);
		
		//1.先删除子表数据
		yyPlaceTagRelMapper.deleteByMainId(yyPlace.getId());
		
		//2.子表数据重新插入
		if(yyPlaceTagRelList!=null && yyPlaceTagRelList.size()>0) {
			for(YyPlaceTagRel entity:yyPlaceTagRelList) {
				//外键设置
				entity.setPlaceId(yyPlace.getId());
				yyPlaceTagRelMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		yyPlaceTagRelMapper.deleteByMainId(id);
		yyPlaceMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			yyPlaceTagRelMapper.deleteByMainId(id.toString());
			yyPlaceMapper.deleteById(id);
		}
	}

	@Override
	public int getCountByPlaceId(String placeId) {
		return yyPlaceMapper.getCountByPlaceId(placeId);
	}

	@Override
	public YyPlace selectByPlaceId(String placeId) {
		YyPlace ret = yyPlaceMapper.selectByPlaceId(placeId);
		return ret;
	}

	@Override
	public List<AutoCompleteText> inputAutocomplete(String text) {
		List<AutoCompleteText> autoCompleteText = new ArrayList<>();
		List<YyPlace> mappingPlaces = new ArrayList<>();

		mappingPlaces = yyPlaceMapper.getPlacesByInput(text);
		AutoCompleteText retText;
		for (YyPlace place:mappingPlaces) {
			// 既に本システムに導入したところの場合、DBから取得
			retText = new AutoCompleteText();
			retText.setPlaceId(place.getGooglePladeId());
			retText.setPlaceText(getResultByLang(place));
			retText.setPlaceAddr(getAddrByLang(place));
			autoCompleteText.add(retText);
		}

		if (autoCompleteText.size() < 3) {
			// Google Place API を利用
			List<AutoCompleteText> autoCompleteTextApi = googlePlacesApi.inputAutocomplete(text);

			Set<String> dbPlaceIdSet = new HashSet<>();
			for (AutoCompleteText p : autoCompleteText) {
				dbPlaceIdSet.add(p.getPlaceId());
			}

			// ② 把 API 中 DB 没有的加入
			for (AutoCompleteText apiPlace : autoCompleteTextApi) {
				if (!dbPlaceIdSet.contains(apiPlace.getPlaceId())) {
					autoCompleteText.add(apiPlace);
					if (autoCompleteText.size() >= 5) {
						break;
					}
				}
			}
		}

		return autoCompleteText;
	}

	private String getResultByLang(YyPlace obj){
		String ret = "";

		switch (LangContext.getLang()){
			case "zh-CN":
				ret = obj.getAddressZh();
				break;
			case "zh-TW":
				ret = obj.getAddressTw();
				break;
			case "ja":
				ret = obj.getAddressJp();
				break;
			default:
				ret = obj.getAddressEn();
				break;
		}

		return ret;
	}
	private String getAddrByLang(YyPlace obj){
		String ret = "";

		switch (LangContext.getLang()){
			case "zh-CN":
				ret = obj.getNameZh();
			case "zh-TW":
				ret = obj.getNameTw();
			case "ja":
				ret = obj.getNameJp();
			default:
				ret = obj.getNameEn();
		}

		return ret;
	}

	@Override
	public List<AutoCompleteText> fetchAirports() {
		List<AutoCompleteText> autoCompleteText = new ArrayList<>();
		List<YyPlace> mappingPlaces = new ArrayList<>();

		mappingPlaces = yyPlaceMapper.getAirports();
		AutoCompleteText retText;
		for (YyPlace place:mappingPlaces) {
			// 既に本システムに導入したところの場合、DBから取得
			retText = new AutoCompleteText();
			retText.setPlaceId(place.getGooglePladeId());
			retText.setPlaceText(getResultByLang(place));
			retText.setPlaceAddr(getAddrByLang(place));
			autoCompleteText.add(retText);
		}
		return autoCompleteText;
	}


}
