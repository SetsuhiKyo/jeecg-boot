package org.jeecg.modules.yy.place.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.config.yyi18n.LangContext;
import org.jeecg.modules.yy.place.entity.YyDiscount;
import org.jeecg.modules.yy.place.entity.YyRoute;
import org.jeecg.modules.yy.place.entity.YyRouteDetail;
import org.jeecg.modules.yy.place.entity.YyRouteDetailPlaces;
import org.jeecg.modules.yy.place.mapper.*;
import org.jeecg.modules.yy.place.service.IYyRouteDetailPlacesService;
import org.jeecg.modules.yy.place.service.IYyRouteDetailService;
import org.jeecg.modules.yy.place.vo.RouteInfoView;
import org.jeecg.modules.yy.place.vo.YyRoutesDtail;
import org.jeecg.modules.yy.place.vo.YyRoutesDtailDay;
import org.jeecg.modules.yy.place.vo.YyRoutesDtailDayPlace;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 行程明细
 * @Author: jeecg-boot
 * @Date:   2025-07-25
 * @Version: V1.0
 */
@Service
public class YyRouteDetailServiceImpl extends ServiceImpl<YyRouteDetailMapper, YyRouteDetail> implements IYyRouteDetailService {
	
	@Autowired
	private YyRouteDetailMapper yyRouteDetailMapper;
	@Autowired
	private YyRouteDetailPlacesMapper yyRouteDetailPlacesMapper;
	@Autowired
	private YyRouteMapper yyRouteMapper;
	@Autowired
	private YyRoutePriceMapper yyRoutePriceMapper;
	@Autowired
	private YyUserReviewMapper yyUserReviewMapper;
	@Autowired
	private YyDiscountMapper yyDiscountMapper;
	@Autowired
	private IYyRouteDetailPlacesService yyRouteDetailPlacesService;

	@Override
	public List<YyRouteDetail> selectByMainId(String mainId) {
		return yyRouteDetailMapper.selectByMainId(mainId);
	}

	/**
	 * 通过行程id查询行程详细情报
	 *
	 * @param routeId
	 * @return YyRoutesDtail
	 */
	@Override
	public YyRoutesDtail getRouteDtailInfo(String routeId) {
		YyRoutesDtail routeInfo = new YyRoutesDtail();
		RouteInfoView reviewInfo ;

		// 行程基本
		YyRoute route = yyRouteMapper.selectById(routeId);

		if (route != null ) {
			routeInfo.setId(route.getId()); // 行程ID
			routeInfo.setTitle(getResultByLang(route)); // 行程标题

			BigDecimal basicPrice = yyRoutePriceMapper.getRouteBasicPrice("04", route.getId()); // 豪华7座的价格作为基本价格
			if (basicPrice != null) {
				routeInfo.setBasicPrice(basicPrice); // 基本价格
				routeInfo.setComputedPrice(computeDiscountPrice(basicPrice)); // 优惠价格
			} else {
				routeInfo.setBasicPrice(new BigDecimal(0)); // 基本价格
				routeInfo.setComputedPrice(new BigDecimal(0)); // 优惠价格
			}
		}

		// 行程详细
		List<YyRouteDetail> routeDetails = yyRouteDetailMapper.selectByMainId(routeId);
		if (routeDetails != null && routeDetails.size() > 0) {
			List<YyRoutesDtailDay> days = new ArrayList<>();
			List<YyRoutesDtailDayPlace> places = new ArrayList<>();
			YyRoutesDtailDay day;
			String imgUrl = "";

			for (YyRouteDetail detail:routeDetails){

				 day = new YyRoutesDtailDay();
				 day.setTitle(getDetailByLang(detail));

				places = yyRouteDetailPlacesMapper.getDetailPlaces(detail.getRouteId(),detail.getId());

				if (places != null && places.size() > 0) {
					for(YyRoutesDtailDayPlace place:places){
						imgUrl = place.getPlaceImg();
						place.setPlaceImg(imgUrl);
					}
				}
				day.setPlaces(places);
				days.add(day);
			}

			routeInfo.setDays(days);
		}

		return routeInfo;
	}

	private String getResultByLang(YyRoute obj){
		String ret = "";

		switch (LangContext.getLang()){
			case "zh-CN":
				ret = obj.getRouteTitleZh();
				break;
			case "zh-TW":
				ret = obj.getRouteTitleTw();
				break;
			case "ja":
				ret = obj.getRouteTitleJp();
				break;
			default:
				ret = obj.getRouteTitleEn();
				break;
		}

		return ret;
	}
	private String getDetailByLang(YyRouteDetail obj){
		String ret = "";

		switch (LangContext.getLang()){
			case "zh-CN":
				ret = obj.getDetailTitleZh();
			case "zh-TW":
				ret = obj.getDetailTitleTw();
			case "ja":
				ret = obj.getDetailTitleJp();
			default:
				ret = obj.getDetailTitleEn();
		}

		return ret;
	}

	private BigDecimal computeDiscountPrice(BigDecimal basic){
		BigDecimal computedPrice = basic;
		BigDecimal rate = new BigDecimal(0.0);

		// 如果当前有系统优惠，计算优惠后价格
		List<YyDiscount> sysDiscounts = yyDiscountMapper.getValidDiscount("01");
		if (sysDiscounts != null && sysDiscounts.size() > 0){
			for(YyDiscount discount : sysDiscounts){
				rate = new BigDecimal(1 - discount.getDiscountRate()/100);
				computedPrice = computedPrice.multiply(rate);
			}
		}

		return computedPrice;
	}
}
