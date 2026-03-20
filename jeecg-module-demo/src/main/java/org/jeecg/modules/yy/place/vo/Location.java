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
public class Location {
    private boolean centralFlg;
    // 纬度
    private double lat;
    // 经度
    private double lng;
}
