package org.jeecg.modules.yy.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeecg.modules.yy.place.entity.YyPlace;
import org.jeecg.modules.yy.place.entity.YyPlaceTagRel;
import org.jeecg.modules.yy.place.service.IYyPlaceTagService;
import org.jeecg.modules.yy.place.vo.AutoCompleteText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


/**
 * Description:
 * 這個API是使用Google Routes API V2 版本
 * 特点：
 *    使用 REST v1
 *    使用 HTTP Header 控制字段
 *    更严格的 Field Mask
 *
 * 实现功能：
 *    1.根据google 地点id计算出俩个地点之间的距离，行车时间，高速费用
 *
 * @Author 姜雪飛
 * @Create 2025/07/02
 * @Version 0.1
 */
@Component
public class GoogleRoutesAPIV2 {

    // Google Map API Key
    @Value("${google.map.api-key}")
    private String GOOGLE_API_KEY;

    // Google Routes API URL -- 计算路线
    @Value("${google.map.url.compute-route}")
    private String GOOGLE_COMPUTE_ROUTE_URL;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 1.根据文本检索地点
     *  @param fromPlaceId:开始地点
     *  @param toPlaceId:终了地点
     *  @return RoutesResponse
     *
     */
    public RoutesResponse computeRouteByPlaceId(
            String fromPlaceId,
            String toPlaceId
        ) {
            RoutesResponse ret = new RoutesResponse();
        try {
            // Header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Goog-Api-Key", GOOGLE_API_KEY);
            headers.set("X-Goog-FieldMask",
                    "routes.distanceMeters," +
                            "routes.duration," +
                            "routes.polyline.encodedPolyline");

            // Body
            RoutesRequest body = new RoutesRequest();

            RoutesRequest.Waypoint origin = new RoutesRequest.Waypoint();
            origin.setPlaceId(fromPlaceId);

            RoutesRequest.Waypoint destination = new RoutesRequest.Waypoint();
            destination.setPlaceId(toPlaceId);

            body.setOrigin(origin);
            body.setDestination(destination);
            body.setTravelMode("DRIVE");
            body.setRoutingPreference("TRAFFIC_UNAWARE");
            body.setLanguageCode("ja");
            body.setUnits("METRIC");

            RoutesRequest.RouteModifiers modifiers = new RoutesRequest.RouteModifiers();
            modifiers.setAvoidHighways(false);
            modifiers.setAvoidTolls(false);
            body.setRouteModifiers(modifiers);

            HttpEntity<RoutesRequest> entity = new HttpEntity<>(body, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<RoutesResponse> response =
                    restTemplate.postForEntity(GOOGLE_COMPUTE_ROUTE_URL, entity, RoutesResponse.class);


            if (response.getStatusCodeValue()==200){
                ret = response.getBody();
            }
        } catch (Exception e) {
            return ret;
        }
//            System.out.println(ret);
            return ret;
        }

    public static void main(String[] args) throws Exception {
        GoogleRoutesAPIV2 testApi = new GoogleRoutesAPIV2();
        testApi.computeRouteByPlaceId("ChIJTz8EpbRfGWARR30zHkuyyyk","ChIJ35ov0dCOGGARKvdDH7NPHX0"); // 河口湖　⇒　東京スカイツリー
    }

}