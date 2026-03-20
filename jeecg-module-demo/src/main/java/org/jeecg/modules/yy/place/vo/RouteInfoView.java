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
public class RouteInfoView {
    private String id;
    private String imgUrl;
    private String title;
    private String[] places;
    private double reviewRate;
    private int reviewNum;
    private BigDecimal basicPrice;
    private BigDecimal computedPrice;
    private String favoriteFlg;

}
