package org.jeecg.modules.yy.api;

import lombok.Data;

/**
 * Description:
 *
 * @Author 姜雪飛
 * @Create 2026/01/15
 * @Version 0.1
 */
@Data
public class RoutesRequest {

    private Waypoint origin;
    private Waypoint destination;
    private String travelMode;
    private String routingPreference;
    private RouteModifiers routeModifiers;
    private String languageCode;
    private String units;
    private Boolean computeAlternativeRoutes;

    @Data
    public static class Waypoint {
        private String placeId;
    }

      @Data
    public static class RouteModifiers {
        private boolean avoidTolls;
        private boolean avoidHighways;
    }
}
