package org.jeecg.modules.yy.api;

import com.graphbuilder.curve.Polyline;
import lombok.Data;

import java.util.List;

/**
 * Description:
 *
 * @Author 姜雪飛
 * @Create 2026/01/15
 * @Version 0.1
 */
@Data
public class RoutesResponse {

    private List<Route> routes;

    @Data
    public static class Route {
        private String distanceMeters;
        private String duration;
        private Polyline polyline;
    }

    @Data
    public static class Polyline {
        private String encodedPolyline;
    }
}

