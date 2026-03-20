package org.jeecg.modules.yy.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeecg.modules.yy.place.entity.YyPlace;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Description:
 * 這個API是使用Google MAP API 的【文本搜索（新）】進行地點搜索的測試代碼。
 *
 * @Author 姜雪飛
 * @Create 2025/07/02
 * @Version 0.1
 */
@Component
public class GooglePlacesTextSearch {

    // Google Map API URL -- 附近搜索
    @Value("${google.map.url.text-search}")
    private String BASE_URL;

    // Google Map API Key
    @Value("${google.map.api-key}")
    private String API_KEY;

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
    public List<YyPlace> searchText(String text,  String resItems) {

        List<YyPlace> placeList = new ArrayList<>();
        try{

            // 构造请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("textQuery", text); // 文本
            requestBody.put("languageCode", "en");
            requestBody.put("maxResultCount", "20");  // 最大20，并且無法判斷是否有下一頁，以及下一頁的數據應該如何獲取。

            String jsonBody = objectMapper.writeValueAsString(requestBody);

            // 设置 HTTP 请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-Goog-Api-Key", "AIzaSyDyB6Y1HCDKW7x6l7hXA3jGQFMqNJl2fYM");
            headers.set("X-Goog-FieldMask", resItems); // 指定返回字段（测试可用 *）

            HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    "https://places.googleapis.com/v1/places:searchText",
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (response.getStatusCodeValue() == 200) {

                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode places = null;
                if (root != null) {
                    places = root.path("places");
                }

                if (places != null) {

                    YyPlace yyPlace;
                    String placeId = "";

                    for (JsonNode place : places) {
                        yyPlace = new YyPlace();
                        placeId = place.path("id").asText();
                        yyPlace.setId(placeId);
                        placeList.add(yyPlace);
                    }
                }
            }

            System.out.println(placeList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return placeList;
    }

    public static void main(String[] args) throws JsonProcessingException {
        GooglePlacesTextSearch testApi = new GooglePlacesTextSearch();
        testApi.searchText("富士山","places.id,places.displayName");
    }
}