package org.jeecg.modules.yy.place.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 *
 * @Author 姜雪飛
 * @Create 2025/07/03
 * @Version 0.1
 */
@Data
public class OrderInfoView {
    private String customerId;
    private String orderNo;
    private String orderNm;
    private String orderStatus;
    private String paymentStatus;
    private String orderType;
    private String orderKbn;
    private String pickupDte;
    private String pickupLoc;
    private String pickupLocNm;
    private String pickupLocAddr;
    private String flightNo;
    private String dropLoc;
    private String dropLocNm;
    private String dropLocAddr;
    private String returnDte;
    private String rtnPickupLoc;
    private String rtnPickupLocNm;
    private String rtnPickupLocAddr;
    private String rtnDorpLoc;
    private String rtnDorpLocNm;
    private String rtnDorpLocAddr;
    private int useHours;
    private String routeDistance;
    private String routeId;
    private String routeNm;
    private String customerNm;
    private int passengerNum;
    private int lunguageNum;
    private String carType;
    private boolean needChdChair;
    private boolean needNameSign;
    private boolean needHotelChkIn;
    private boolean needFlightChkIn;
    private String disCountCd;
    private BigDecimal computedPrice;

}
