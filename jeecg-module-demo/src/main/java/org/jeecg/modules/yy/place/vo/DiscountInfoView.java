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
public class DiscountInfoView {
    private String img;
    private String validyDate;
    private boolean isInvalid;
    private String invalidStuts;
    private String discountStatus;
}
