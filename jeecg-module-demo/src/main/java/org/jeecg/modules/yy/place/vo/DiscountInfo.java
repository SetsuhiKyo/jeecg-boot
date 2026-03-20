package org.jeecg.modules.yy.place.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Description:
 *
 * @Author 姜雪飛
 * @Create 2025/07/03
 * @Version 0.1
 */
@Data
public class DiscountInfo {
    private String disId;
    private String disName;
    private String disType;
    private String disKbn;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer disRate;        // INT → Integer
    private BigDecimal disAmount;   // DECIMAL → BigDecimal
    private String used;
}

