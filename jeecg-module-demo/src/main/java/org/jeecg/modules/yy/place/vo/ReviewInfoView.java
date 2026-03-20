package org.jeecg.modules.yy.place.vo;

import lombok.Data;

/**
 * Description:
 *
 * @Author 姜雪飛
 * @Create 2025/07/03
 * @Version 0.1
 */
@Data
public class ReviewInfoView {
    private String userId;
    private String userName;
    private String countryImg;
    private String reviewTime;
    private String routeId;
    private String routeTitle;
    private double routeReview;
    private String reviewContent;

}
