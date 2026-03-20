package org.jeecg.modules.yy.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeecg.modules.yy.place.entity.YyPlace;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Description:
 * 這個API是使用Wikipedia API 的地點名稱（盡量用英文）進行地點搜索的測試代碼。
 *
 * @Author 姜雪飛
 * @Create 2025/07/02
 * @Version 0.1
 */
@Component
public class WikipediaPlaceSearch {

    // Wikipedia API URL -- GEOSEARCH
    @Value("${wikipedia.place.url.geosearch}")
    private String WIKI_GEOSEARCH_URL;

    // Wikipedia API URL -- SUMMARY 英文
    @Value("${wikipedia.place.url.summary-en}")
    private String WIKI_SUMMARY_EN_URL;

    // Wikipedia API URL -- SUMMARY 日文
    @Value("${wikipedia.place.url.summary-ja}")
    private String WIKI_SUMMARY_JA_URL;

    // Wikipedia API URL -- SUMMARY 中文
    @Value("${wikipedia.place.url.summary-zh}")
    private String WIKI_SUMMARY_ZH_URL;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 根据坐标查询附近 Wikipedia 条目
     */
    public Map<String, Object> geoSearch(double lat, double lng, int radius, int limit) {
        URI uri = UriComponentsBuilder.fromHttpUrl(WIKI_GEOSEARCH_URL)
                .queryParam("action", "query")
                .queryParam("list", "geosearch")
                .queryParam("gscoord", lat + "|" + lng)
                .queryParam("gsradius", radius)
                .queryParam("gslimit", limit)
                .queryParam("format", "json")
                .build(StandardCharsets.UTF_8);

        return restTemplate.getForObject(uri, Map.class);
    }

    /**
     * 根据 Wikipedia 条目名称获取摘要
     */
    public String getSummary(String lang,String title) {
        try {
            // title 中的空格需替换为下划线
            String word = title.replace(" ", "_");
            String url = "";
            if ("en".equals(lang)) {
                url = WIKI_SUMMARY_EN_URL + word;
            } else if ("ja".equals(lang)) {
                url = WIKI_SUMMARY_JA_URL + word;
            } else if ("zh".equals(lang)) {
                url = WIKI_SUMMARY_ZH_URL + word;
            } else {
                url = WIKI_SUMMARY_EN_URL + word;
            }

            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            return root.path("extract").asText();
        } catch (Exception e) {
            return "";
        }
    }

    public static void main(String[] args) throws JsonProcessingException {
        WikipediaPlaceSearch wikipediaClient = new WikipediaPlaceSearch();
        // 查询东京塔附近 1 公里范围内的 Wikipedia 条目
//        var geoResult = wikipediaClient.geoSearch(35.6586, 139.7454, 1000, 5);
//        System.out.println("Geosearch Result: " + geoResult);

        // 获取 Tokyo Tower 的摘要
        String summary = wikipediaClient.getSummary("ja","実物大ユニコーンガンダム立像");
        System.out.println("Summary: " + summary);
    }
}