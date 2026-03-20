package org.jeecg.modules.yy.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jeecg.modules.yy.place.entity.YyPlace;
import org.jeecg.modules.yy.place.entity.YyPlaceTagRel;
import org.jeecg.modules.yy.place.service.IYyPlaceTagRelService;
import org.jeecg.modules.yy.place.service.IYyPlaceTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;


/**
 * Description:
 * 這個API是使用Google MAP API 的【地點詳細（新）】進行地點情報取得的測試代碼。
 *
 * @Author 姜雪飛
 * @Create 2025/07/02
 * @Version 0.1
 */
@Component
public class GooglePlacesDetails {

    @Autowired
    private IYyPlaceTagService yyPlaceTagService;

    @Autowired
    private WikipediaPlaceSearch wikiPlaceSearch;

    // Google Map API URL -- 地點詳細
    @Value("${google.map.url.place-details}")
    private String GOOGLE_PLACE_DETAIL_URL;

    // Google Map API URL -- 圖片信息取得
    @Value("${google.map.url.place-photos}")
    private String GOOGLE_PLACE_PHOTOS_URL;

    // Google Map API 參數 -- 圖片信息取得
    @Value("${google.map.param.place-photos}")
    private String GOOGLE_PLACE_PHOTOS_PARAM;

    // Google Map API Key
    @Value("${google.map.api-key}")
    private String GOOGLE_MAP_API_KEY;

    // Google Map IMG Download Path
    @Value("${google.map.download-path.img}")
    private String GOOGLE_MAP_DOWN_PATH_IMG;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 取得地点詳細
     *  place:地點情報
     *  userNums:地點評價用戶數
     *  fields:返回地點字段（可複數）
     *
     */
    public List<YyPlaceTagRel> getPlaceDetails(YyPlace place, int userNums, String fields) throws Exception {

        List<YyPlaceTagRel> rets = new ArrayList<>();
        try{
            // 日文版詳細情報取得
            String url = UriComponentsBuilder.fromHttpUrl(GOOGLE_PLACE_DETAIL_URL + place.getGooglePladeId())
                    .queryParam("fields", fields)
                    .queryParam("languageCode", "ja")
                    .queryParam("key", GOOGLE_MAP_API_KEY)
                    .toUriString();

            String response = restTemplate.getForObject(url, String.class);

            JsonNode root = objectMapper.readTree(response);

            // google_plade_id

            // userRatingCount Google地點評價用戶數取得
            int userRatingCount = 0;
            if (root.path("userRatingCount") != null && !root.path("userRatingCount").asText().isEmpty()) {
                userRatingCount = Integer.parseInt(root.path("userRatingCount").asText());
            }

            // Google地點評價用戶數 < 10 人的話就不作爲采集對象（評價點數 > 4.0 的條件是在附近檢索裏實現的）
            if (userRatingCount < userNums) {
                return rets;
            }

            // Google_Types
            String[] types = objectMapper.convertValue(root.path("types"), String[].class);
            String google_types = "";
            for (int i = 0 ; i < types.length ; i++){
                if (i == 0) {
                    google_types = google_types + types[i];
                } else {
                    google_types = google_types + "," + types[i];
                }
            }
            place.setGoogleTypes(google_types);
            // google_primary_type
            place.setGooglePrimaryType(root.path("primaryType").asText());

            // name_jp
            place.setNameJp(root.path("displayName").path("text").asText());
            // name_zh
            place.setNameZh(root.path("displayName").path("text").asText());
            // name_tw
            place.setNameTw(root.path("displayName").path("text").asText());

            // summary_jp
            place.setSummaryJp(root.path("editorialSummary").path("text").asText());
            // summary_zh
            place.setSummaryZh(root.path("editorialSummary").path("text").asText());
            // summary_tw
            place.setSummaryTw(root.path("editorialSummary").path("text").asText());

            // detailinfo_jp
            // detailinfo_zh
            // detailinfo_tw
            // detailinfo_en

            // address_jp
            place.setAddressJp(root.path("formattedAddress").asText());
            // address_zh
            place.setAddressZh(root.path("formattedAddress").asText());
            // address_tw
            place.setAddressTw(root.path("formattedAddress").asText());

            // latitude
            place.setLatitude(root.path("location").path("latitude").asDouble(0.0));
            // longitude
            place.setLongitude(root.path("location").path("longitude").asDouble(0.0));
            // cost_time
            // telphone_no
            place.setTelphoneNo(root.path("internationalPhoneNumber").asText());
            // site_url
            place.setSiteUrl(root.path("websiteUri").asText());
            // can_proxy
            place.setCanProxy(root.path("reservable").asText());
            // place_map_url
            // rating Google地點評價分數取得
//            double rating = 0.0;
//            if (root.path("rating") != null && !root.path("rating").asText().isEmpty()) {
//                rating = Double.parseDouble(root.path("rating").asText());
//            }
//            place.setUserRating(rating);

            // 關於圖片：Place Photos API 不在 Google Maps Platform 的 $200 免费额度之内，它是单独计费的。
            // Google Maps Platform 提供的 $200/月免费额度主要适用于：
            //     Maps JavaScript API
            //     Places API（Details、Nearby Search 等）
            //     Geocoding、Directions 等 API
            // 所以，在本系統的前期決定不使用Google的圖片。改成使用人工收集的圖片或者從免費圖片網站獲取。
            // 所有圖片都將通過後臺管理系統人工上傳及管理。
//            List<Object> photos = objectMapper.convertValue(root.path("photos"), List.class);
//            if (photos != null && photos.size() > 0) {
//                String imgUrlName = "";
//                // place_img_url
//                imgUrlName = root.path("photos").path(0).path("name").asText();
//                place.setPlaceImgUrl(downloadPlacePhoto(imgUrlName,place.getGooglePladeId()));
//
//                // imgs_url1～5
//                if (photos.size() > 1) {
//                    imgUrlName = root.path("photos").path(1).path("name").asText();
//                    place.setImgsUrl1(downloadPlacePhoto(imgUrlName,place.getGooglePladeId()));
//                }
//                if (photos.size() > 2) {
//                    imgUrlName = root.path("photos").path(2).path("name").asText();
//                    place.setImgsUrl2(downloadPlacePhoto(imgUrlName,place.getGooglePladeId()));
//                }
//                if (photos.size() > 3) {
//                    imgUrlName = root.path("photos").path(3).path("name").asText();
//                    place.setImgsUrl3(downloadPlacePhoto(imgUrlName,place.getGooglePladeId()));
//                }
//                if (photos.size() > 4) {
//                    imgUrlName = root.path("photos").path(4).path("name").asText();
//                    place.setImgsUrl4(downloadPlacePhoto(imgUrlName,place.getGooglePladeId()));
//                }
//                if (photos.size() > 5) {
//                    imgUrlName = root.path("photos").path(5).path("name").asText();
//                    place.setImgsUrl5(downloadPlacePhoto(imgUrlName,place.getGooglePladeId()));
//                }
//            }
            // info_source
            place.setInfoSource("");

            // 英文版詳細情報取得
            url = UriComponentsBuilder.fromHttpUrl(GOOGLE_PLACE_DETAIL_URL + place.getGooglePladeId())
                    .queryParam("fields", "displayName,formattedAddress,editorialSummary")
                    .queryParam("languageCode", "en")
                    .queryParam("key", GOOGLE_MAP_API_KEY)
                    .toUriString();

            response = restTemplate.getForObject(url, String.class);

            root = objectMapper.readTree(response);
            // name_en
            place.setNameEn(root.path("displayName").path("text").asText());
            // address_en
            place.setAddressEn(root.path("formattedAddress").asText());
            // summary_en
            place.setSummaryEn(root.path("editorialSummary").path("text").asText());

            // 簡體中文版詳細情報取得
//            url = UriComponentsBuilder.fromHttpUrl(GOOGLE_PLACE_DETAIL_URL + place.getGooglePladeId())
//                    .queryParam("fields", "displayName,formattedAddress")
//                    .queryParam("languageCode", "zh-CN")
//                    .queryParam("key", GOOGLE_MAP_API_KEY)
//                    .toUriString();
//
//            response = restTemplate.getForObject(url, String.class);
//
//            root = objectMapper.readTree(response);
//            // name_zh
//            place.setNameZh(root.path("displayName").path("text").asText());
//            // address_zh
//            place.setAddressZh(root.path("formattedAddress").asText());

            // 繁體中文版詳細情報取得
//            url = UriComponentsBuilder.fromHttpUrl(GOOGLE_PLACE_DETAIL_URL + place.getGooglePladeId())
//                    .queryParam("fields", "displayName,formattedAddress")
//                    .queryParam("languageCode", "zh-TW")
//                    .queryParam("key", GOOGLE_MAP_API_KEY)
//                    .toUriString();
//
//            response = restTemplate.getForObject(url, String.class);
//
//            root = objectMapper.readTree(response);
//            // name_tw
//            place.setNameTw(root.path("displayName").path("text").asText());
//            // address_tw
//            place.setAddressTw(root.path("formattedAddress").asText());


            // 關於地點摘要：對於使用Google Map Place Detail API無法取得摘要的地點，
            // 再次使用Wikipedia REST API 取得地點摘要
            String summary = "";
            if (place.getSummaryJp() == null || place.getSummaryJp().isEmpty()){
                summary = wikiPlaceSearch.getSummary("ja",place.getNameJp());
                // summary_jp
                place.setSummaryJp(summary);
                // summary_zh
                place.setSummaryZh(summary);
                // summary_tw
                place.setSummaryTw(summary);
            }
            if (place.getSummaryEn() == null || place.getSummaryEn().isEmpty()){
                summary = wikiPlaceSearch.getSummary("en",place.getNameEn());
                // summary_en
                place.setSummaryEn(summary);
            }

            // 根據地點類型取得標簽編碼
            List<String> typeTags = new ArrayList<>();
            if (types != null && types.length > 0){
                typeTags = yyPlaceTagService.getTagCdByPrimaryType(Arrays.asList(types));

                YyPlaceTagRel yyPlaceTagRel;
                for(int i = 0 ; i < typeTags.size() ; i++){
                    yyPlaceTagRel = new YyPlaceTagRel();
                    yyPlaceTagRel.setPlaceId(place.getGooglePladeId());
                    yyPlaceTagRel.setTagCd(typeTags.get(i));

                    rets.add(yyPlaceTagRel);
                }
            }

        } catch (Exception e) {
            throw e;
        }
        return rets;
    }

    /**
     *
     * @param photoReference
     * @return 本地保存圖片的URL
     */
    public String downloadPlacePhoto(String photoReference, String placeId) throws Exception {

        try {
            // Step 1: 构建 Google Place Photo API 请求
            String googleApiUrl = UriComponentsBuilder.fromHttpUrl(GOOGLE_PLACE_PHOTOS_URL + photoReference + "/media")
                    .queryParam("maxWidthPx", GOOGLE_PLACE_PHOTOS_PARAM)
                    .queryParam("key", GOOGLE_MAP_API_KEY)
                    .toUriString();

            // 设置 header，接收图片内容
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.IMAGE_JPEG));
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            // 发起请求，直接获取图片字节流
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    googleApiUrl,
                    HttpMethod.GET,
                    entity,
                    byte[].class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                byte[] imageBytes = response.getBody();
                String fileName = placeId + "_" + UUID.randomUUID() + ".jpg";
                Path path = Paths.get(GOOGLE_MAP_DOWN_PATH_IMG, fileName);
                // 创建目录
//                Files.createDirectories(path.getParent());
                Files.write(path, imageBytes);
                return path.toString().substring(13);
            } else {
                return "Failed to get image redirection from Google API";
            }

        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * 取得地点詳細
     *  placeId:地點ID
     */
    public String getPlaceName(String placeId)  {
        String placeName = "";
        try {
            // 日文版地点名称取得
            String url = UriComponentsBuilder.fromHttpUrl(GOOGLE_PLACE_DETAIL_URL + placeId)
                    .queryParam("fields", "displayName")
                    .queryParam("languageCode", "ja")
                    .queryParam("key", GOOGLE_MAP_API_KEY)
                    .toUriString();
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            placeName = root.path("displayName").path("text").asText();
         } catch (Exception e) {
        e.printStackTrace();
    }
        return placeName;
    }
    public static void main(String[] args) throws JsonProcessingException {
        try {
            GooglePlacesDetails testApi = new GooglePlacesDetails();
            YyPlace tester = new YyPlace();
            tester.setGooglePladeId("ChIJmcj9QppiGWAR36TzFsn8oaY"); //富士山
            testApi.getPlaceDetails(tester, 50,"id,displayName,types,primaryType,formattedAddress,location,internationalPhoneNumber,websiteUri,reservable,photos,editorialSummary");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}