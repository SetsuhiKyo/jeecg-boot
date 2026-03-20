package org.jeecg.modules.yy.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.jeecg.modules.yy.place.entity.YyPlace;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Description:
 * 這個API是使用Google MAP API 的【附近搜索（新）】進行地點搜索的測試代碼。
 * 由於本API做成時點（20250701），Google官方還沒有對此新API的分頁策略給出明確的方法。
 * 所以目前每次查詢結果最多返回20條。
 *
 * @Author 姜雪飛
 * @Create 2025/07/02
 * @Version 0.1
 */
@Component
public class GooglePlacesNearbySearch {

    // Google Map API URL -- 附近搜索
    @Value("${google.map.url.nearby-search}")
    private String GOOGLE_NEARBY_SEARCH_URL;

    // Google Map API Key
    @Value("${google.map.api-key}")
    private String GOOGLE_MAP_API_KEY;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 搜索附近地点
     *  lat:中心坐標緯度
     *  lng:中心坐標經度
     *  radius:搜索半徑
     *  types:地點類型（可複數）
     *  resItems:返回地點字段（可複數）
     */
    public List<YyPlace> searchNearby(Double lat, Double lng, String radius,Double userRating, String type, String resItems) {

        List<YyPlace> placeList = new ArrayList<>();
        try{

            // 构造请求体
            Map<String, Object> location = new HashMap<>();
            location.put("latitude", lat);
            location.put("longitude", lng);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("locationRestriction", Map.of("circle", Map.of(
                    "center", location,
                    "radius", radius
            )));
            requestBody.put("includedPrimaryTypes", type); // 类型可选
//            requestBody.put("languageCode", "en");
            requestBody.put("maxResultCount", "20");  // 最大20，并且無法判斷是否有下一頁，以及下一頁的數據應該如何獲取。

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(requestBody);

            // 设置 HTTP 请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Goog-Api-Key", GOOGLE_MAP_API_KEY);
            headers.set("X-Goog-FieldMask", resItems); // 指定返回字段（测试可用 *）

            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            // 以指定的坐標爲中心，在指定的半徑範圍内進行指定的類型的地點檢索
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    GOOGLE_NEARBY_SEARCH_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // 返回正常
            if (response.getStatusCodeValue() == 200) {

                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode places = null;
                if (root != null) {
                    places = root.path("places");
                }

                if (places != null) {

                    YyPlace yyPlace;
                    String placeId = "";
                    double rating = 0.0;
                    for (JsonNode place : places) {

                        yyPlace = new YyPlace();
                        // Google地點ID取得
                        placeId = place.path("id").asText();
                        // Google地點評價分數取得
                        if (place.path("rating") != null && !place.path("rating").asText().isEmpty()) {
                            rating = Double.parseDouble(place.path("rating").asText());
                        }
                        // 評價分數在4星以上，作爲地點采集對象
                        if (rating > userRating) {
                            yyPlace.setGooglePladeId(placeId);
                            placeList.add(yyPlace);
                        }
                    }
                }
            }

        } catch (Exception e) {
            return placeList;
        }

        return placeList;
    }

//    public static void main(String[] args) throws JsonProcessingException {
//        GooglePlacesNearbySearch testApi = new GooglePlacesNearbySearch();
//        testApi.searchNearby(35.681236,139.767125,"2000","restaurant","places.id,places.displayName");
//    }
}