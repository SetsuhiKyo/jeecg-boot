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
public class ReviewInfoPage {
    private String userId;
    private String orderNo;
    private double appRate;
    private String appContent;
    private double serviceRate;
    private String serviceContent;
    private double routeRate;
    private String routeContent;
    private double driverRate;
    private String driverContent;
    private double carRate;
    private String carContent;

}
