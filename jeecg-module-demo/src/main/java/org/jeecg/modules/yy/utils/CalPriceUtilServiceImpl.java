package org.jeecg.modules.yy.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.jeecg.modules.yy.place.entity.YyRoutePrice;
import org.jeecg.modules.yy.place.entity.YySimpleCost;
import org.jeecg.modules.yy.place.entity.YySimplePriceCoeff;
import org.jeecg.modules.yy.place.service.IYyRoutePriceService;
import org.jeecg.modules.yy.place.service.IYySimpleCostService;
import org.jeecg.modules.yy.place.service.IYySimplePriceCoeffService;
import org.jeecg.modules.yy.place.vo.OrderInfoView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * 车务订单价格计算工具类
 * 目前实现的知识平台上线初期的简易版本，以后规模大了以后需要重新制定定价策略
 *
 * 1. 首先根据订单的距离，时间，订单种别，订单区分，车辆种别计算基本成本
 *      1.1 机场接送时，根据机场，车辆查出简易成本表中事先登录的成本价，直接使用。
 *          1.1.1 机场成本根据机场的不同，覆盖的到目的地的距离范围不同。
 *              如：成田接送覆盖90公里，羽田接送覆盖40公里
 *          1.1.2 目前简易成本表中只登录了成田机场和羽田机场的数值。
 *          1.1.3 超过覆盖范围的部分，利用超过的距离需要再次加算。超出部分的计算也要按照距离范围改变每公里的成本单价
 *               如：超过0-50公里，舒适7座的情况，每公里加算450日元
 *          公式：机场覆盖成本 + 超出部分距离 * 该车型对应的每公里的成本（超过0-50公里，舒适7座的情况，每公里加算450日元）
 *      1.2 一般接送时，根据距离计算
 *          1.2.1 根据距离的计算也要按照距离范围改变每公里的成本单价
 *          1.2.2 并且，一般接送中的往返的情况，要把返程的部分也加算进来
 *          公式： （去成距离 + 返程距离 ） * 该车型对应的每公里的成本（超过0-50公里，舒适7座的情况，每公里加算450日元）
 *      1.3 按小时包车时，根据用车时间计算
 *          1.3.1 要区分整个用车时间段内，哪些部分是通常时间，哪些部分是深夜早朝时间后，再根据各车种的成本单价分别计算
 *          公式： 通常用车时间部分 * 该车种的每小时成本单价  + 深夜早朝用车时间部分 * 该车种的每小时成本单价
 *      1.4 按行程包车时，这里不需要额外计算，直接使用创建行程时登录的各车种的固定成本。
 * 2. 在基本成本的基础上，适用价格计算系数
 *      2.1 目前适用于价格试算系数的只用两种，一个是淡旺季，一个实顾客区域。根据不同的时间，来自不同国家的人显示各自能接受的价格
 *      2.2 以后根据实际情况可以在这个处理里继续追加其他有需要的系数
 *      公式： 订单成本 * 过客所属地域系数 * 淡旺季系数
 * 3. 返回适用完价格系数后的价格
 *      3.1 将最终计算后的价格进行取整
 *      如： 14560 -》 15000， 14321 -》 14000
 *      3.2 订单基本价格返回
 *
 * @Author 姜雪飛
 * @Create 2026/02/23
 * @Version 0.1
 */
@Service
public class CalPriceUtilServiceImpl {
    @Autowired
    public IYySimpleCostService yySimpleCostService;
    @Autowired
    public IYyRoutePriceService yyRoutePriceService;
    @Autowired
    public IYySimplePriceCoeffService yySimplePriceCoeffService;

    /**
     * 计算出租车价格
     */
    public  BigDecimal calTaxiPrice( int distance) throws Exception{
        BigDecimal taxiPrice = new BigDecimal(0);
        taxiPrice= taxiPrice.add(new BigDecimal(500)).add(new BigDecimal(400).multiply(new BigDecimal(distance)));
        return taxiPrice;
    }

    /**
     * 计算订单价格
     */
    public  BigDecimal calBasicPrice(OrderInfoView orderInfo, int distance,String lang) throws Exception{
        BigDecimal basicPrice = new BigDecimal(0);
        basicPrice= applyPriceCoeff(calBasicCost(orderInfo,distance),lang);
        return roundBasicPrice(basicPrice);
    }
    /**
     * 计算基本成本
     */
    private  BigDecimal calBasicCost(OrderInfoView orderInfo, int distance) throws Exception{
        // 基本成本
        BigDecimal basicCost = new BigDecimal(0);
        // 订单类型
        String orderType = orderInfo.getOrderType();
        // 订单区分
        String orderKbn = orderInfo.getOrderKbn();
        // 用车时间
        int useHours = orderInfo.getUseHours();
        // 车辆种别
        String carType = orderInfo.getCarType();
        // 行程ID
        String routeId = orderInfo.getRouteId();
        // 成本计算区分
        String calCostKbn = "";
        QueryWrapper queryWrapper = new QueryWrapper();
        YySimpleCost simpleCost = new YySimpleCost();
        YyRoutePrice routePriceInfo = new YyRoutePrice();
        List<YySimpleCost> simpleCostList = new ArrayList<>();

        if ("01".equals(orderType)) {
            // 机场接送订单
            // 机场代码取得
            String airportCd = "";
            if ("1".equals(orderKbn)) {
                // 接机
                airportCd = orderInfo.getPickupLoc();
            }
            if ("2".equals(orderKbn)) {
                // 送机
                airportCd = orderInfo.getDropLoc();
            }
            if ("ChIJVze90XnzImARoRp3YqEpbtU".equals(airportCd)) {
                calCostKbn = "07"; // 成本计算区分：成田机场接送
            } else if ("ChIJ45IxpAtkGGAR3_hG0anDMg0".equals(airportCd)) {
                calCostKbn = "08"; // 成本计算区分：羽田机场接送
            } else if ("ChIJ9_rNIxO5AGARiI-QjZ-ncfE".equals(airportCd)) {
                calCostKbn = "09"; // 成本计算区分：TODO:关西机场接送
            }
            // 从简易成本表中取出成本值
            queryWrapper = new QueryWrapper();
            queryWrapper.eq("cal_cost_type", "03"); // 按机场接送取得成本
            queryWrapper.eq("cal_cost_kubun", calCostKbn);
            queryWrapper.eq("car_type", carType);
            simpleCost  = yySimpleCostService.getOne(queryWrapper);
            basicCost = basicCost.add(simpleCost.getCalCostValue());

            // 根据距离判断是否超出机场接送默认范围
            boolean addCalcFlg = false ;
            BigDecimal overDistance = new BigDecimal(0);
            if ("07".equals(calCostKbn) && distance > 90) {
                // 成田机场接送
                addCalcFlg = true;
                overDistance = overDistance.add(new BigDecimal(distance - 90));
            } else if ("08".equals(calCostKbn) && distance > 40) {
                // 羽田机场接送
                addCalcFlg = true;
                overDistance = overDistance.add(new BigDecimal(distance - 40));
            } else if ("09".equals(calCostKbn) && distance > 40) {
                // TODO:关西机场接送
                addCalcFlg = true;
                overDistance = overDistance.add(new BigDecimal(distance - 40));
            }

            // 对超出机场接送默认范围的部分进行最加成本计算
            if (addCalcFlg){
                queryWrapper = new QueryWrapper();
                queryWrapper.eq("cal_cost_type", "01"); // 按距离单价取得成本
                queryWrapper.eq("car_type", carType);
                simpleCostList  = yySimpleCostService.list(queryWrapper);

                basicCost = basicCost.add(calCostByDistance(overDistance,simpleCostList));
            }
        } else if ("02".equals(orderType)) {
            // 一般接送订单
            queryWrapper = new QueryWrapper();
            queryWrapper.eq("cal_cost_type", "01"); // 按距离单价取得成本
            queryWrapper.eq("car_type", carType);
            simpleCostList  = yySimpleCostService.list(queryWrapper);

            basicCost = basicCost.add(calCostByDistance(new BigDecimal(distance),simpleCostList));
        } else if ("03".equals(orderType)) {
            // 按小时包车订单
            queryWrapper = new QueryWrapper();
            queryWrapper.eq("cal_cost_type", "02"); // 按用车时间取得成本
            queryWrapper.eq("cal_cost_kubun", "01"); // TODO:暂定都是通常时间
            queryWrapper.eq("car_type", carType);
            simpleCost  = yySimpleCostService.getOne(queryWrapper);
            basicCost = basicCost.add(simpleCost.getCalCostValue().multiply(new BigDecimal(useHours)));
        } else if ("04".equals(orderType)) {
            // 按行程包车订单
            queryWrapper = new QueryWrapper();
            queryWrapper.eq("route_id", routeId);
            queryWrapper.eq("car_type", carType);
            routePriceInfo  = yyRoutePriceService.getOne(queryWrapper);
            basicCost = basicCost.add(routePriceInfo.getBasicPrice());
        }
        return basicCost;
    }
    /**
     * 适用价格计算系数
     */
    private  BigDecimal applyPriceCoeff(BigDecimal basicPrice, String lang) throws Exception{
        // 淡旺季系数
        BigDecimal timeCoeff = new BigDecimal(1);
        // 游客所属区域系数
        BigDecimal countryCoeff = new BigDecimal(1);
        // 游客所属区域代码
        String customerCountryCd = "";
        YySimplePriceCoeff simplePriceCoeff = new YySimplePriceCoeff();
        QueryWrapper queryWrapper = new QueryWrapper();


        queryWrapper.eq("price_coeff_type", "01"); // 淡旺季
        queryWrapper.eq("price_coeff_kbn", getSeasonCodeByMonth()); // 判断当前是什么月份
        simplePriceCoeff  = yySimplePriceCoeffService.getOne(queryWrapper);
        timeCoeff = BigDecimal.valueOf(simplePriceCoeff.getCoeff());

        switch (lang) {
            case "zh-CN":
                customerCountryCd = "07";
            case "zh-TW":
                customerCountryCd = "08";
            case "en-US":
                customerCountryCd = "10";
            case "ja-JP":
                customerCountryCd = "11";
            default:
                customerCountryCd = "07";
        }
        queryWrapper = new QueryWrapper();
        queryWrapper.eq("price_coeff_type", "02"); // 顾客所属地域
        queryWrapper.eq("price_coeff_kbn", customerCountryCd); // 游客所属区域代码
        simplePriceCoeff  = yySimplePriceCoeffService.getOne(queryWrapper);
        countryCoeff = BigDecimal.valueOf(simplePriceCoeff.getCoeff());

        return basicPrice.multiply(timeCoeff).multiply(countryCoeff);
    }

    /**
     * 千位四舍五入
     */
    public  BigDecimal roundBasicPrice(BigDecimal value) {
        BigDecimal basicPrice = new BigDecimal(0);
        if (value != null) {
            basicPrice = value.divide(BigDecimal.valueOf(1000), 0, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(1000));
        }
        return basicPrice;
    }

    private  BigDecimal calCostByDistance(BigDecimal distanceKm,List<YySimpleCost> simpleCostList) {
        BigDecimal price = BigDecimal.ZERO;

        BigDecimal km50 = new BigDecimal("50");
        BigDecimal km150 = new BigDecimal("150");
        BigDecimal km300 = new BigDecimal("250");

        BigDecimal rate1 = new BigDecimal("350");
        BigDecimal rate2 = new BigDecimal("280");
        BigDecimal rate3 = new BigDecimal("220");
        BigDecimal rate4 = new BigDecimal("180");
        if (simpleCostList != null && simpleCostList.size() > 3) {
             rate1 = simpleCostList.get(0).getCalCostValue();
             rate2 = simpleCostList.get(1).getCalCostValue();
             rate3 = simpleCostList.get(2).getCalCostValue();
             rate4 = simpleCostList.get(3).getCalCostValue();
        }

        BigDecimal remaining = distanceKm;

        // 0-50 km
        BigDecimal seg = remaining.min(km50);
        price = price.add(seg.multiply(rate1));
        remaining = remaining.subtract(seg);

        // 50-150 km
        if (remaining.compareTo(BigDecimal.ZERO) > 0) {
            seg = remaining.min(km150.subtract(km50)); // 100km
            price = price.add(seg.multiply(rate2));
            remaining = remaining.subtract(seg);
        }

        // 150-300 km
        if (remaining.compareTo(BigDecimal.ZERO) > 0) {
            seg = remaining.min(km300.subtract(km150)); // 150km
            price = price.add(seg.multiply(rate3));
            remaining = remaining.subtract(seg);
        }

        // 300km+
        if (remaining.compareTo(BigDecimal.ZERO) > 0) {
            price = price.add(remaining.multiply(rate4));
        }

        return price;
    }

//    public static void main(String[] args) {
//        System.out.println(calcPrice(new BigDecimal("30")));   // 30*350
//        System.out.println(calcPrice(new BigDecimal("100")));  // 50*350 + 50*280
//        System.out.println(calcPrice(new BigDecimal("250")));  // 50*350 + 100*280 + 100*220
//        System.out.println(calcPrice(new BigDecimal("400")));  // 50*350 + 100*280 + 150*220 + 100*180
//    }


    /**
     * 根据月份返回季节代码
     */
    public static String getSeasonCodeByMonth() {
        int month = LocalDate.now().getMonthValue();
        switch (month) {
            case 1:
            case 2:
                return "01"; // 淡季
            case 3:
            case 4:
                return "02"; // 樱花季
            case 5:
            case 6:
                return "03"; // 初夏
            case 7:
            case 8:
                return "04"; // 暑假
            case 10:
            case 11:
                return "05"; // 红叶季
            case 12:
                return "06"; // 年末年始
            default:
                return "00"; // 普通季 / 未定义（如9月）
        }
    }

}
