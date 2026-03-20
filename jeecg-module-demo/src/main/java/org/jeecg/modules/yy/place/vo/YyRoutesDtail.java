package org.jeecg.modules.yy.place.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description:
 *
 * @Author 姜雪飛
 * @Create 2025/07/03
 * @Version 0.1
 */
@Data
public class YyRoutesDtail {
    private String id;
    private String title;
    private String[] places;
    private double reviewRate;
    private int reviewNum;
    private BigDecimal basicPrice;
    private BigDecimal computedPrice;
    private List<YyRoutesDtailDay> days;
}
