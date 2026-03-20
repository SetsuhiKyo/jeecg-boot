package org.jeecg.modules.yy.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeecg.modules.yy.place.entity.YyPlace;
import org.jeecg.modules.yy.place.vo.AutoCompleteText;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Description:
 * 這個API是使用Google MAP API 的【入力补完】功能的測試代碼。
 *
 * @Author 姜雪飛
 * @Create 2025/09/26
 * @Version 0.1
 */
@Component
public class GooglePlacesAutoComplete {

    // Google Map API URL -- 地点入力自动补完
    @Value("${google.map.url.input-autocomplete}")
    private String BASE_URL;

    // Google Map API Key
    @Value("${google.map.api-key}")
    private String API_KEY;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 地点入力自动补完
     *  text:入力地点
     *  resItems:返回地點字段（可複數）
     */
    public List<AutoCompleteText> inputAutocomplete(String text) {

        List<AutoCompleteText> autoCompleteList = new ArrayList<>();

        try{
            String url = BASE_URL + "?input=" + text + "&key=" + API_KEY;

            String response = restTemplate.getForObject(url, String.class);

            JsonNode root = objectMapper.readTree(response);
            AutoCompleteText item;
            if (root.isObject()) { // オブジェクトの場合
                for (JsonNode field : root.path("predictions")) { // JsonNodeは直接イテレート可能
                    item = new AutoCompleteText();
                    if (field.path("place_id") != null){
                        item.setPlaceId(field.path("place_id").asText());
                    }
                    if (field.path("structured_formatting") != null && field.path("structured_formatting").path("main_text") != null) {
                        item.setPlaceText(field.path("structured_formatting").path("main_text").asText());
                    }
                    if (field.path("structured_formatting") != null && field.path("structured_formatting").path("secondary_text") != null) {
                        item.setPlaceAddr(field.path("structured_formatting").path("secondary_text").asText());
                    }
                    autoCompleteList.add(item);
                }
            }
        } catch (Exception e) {
            return autoCompleteList;
        }

        return autoCompleteList;
    }

//    public static void main(String[] args) throws JsonProcessingException {
//        GooglePlacesAutoComplete testApi = new GooglePlacesAutoComplete();
//        testApi.inputAutocomplete("富士","places.id,places.displayName");
//    }
}