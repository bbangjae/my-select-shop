package com.example.myselectshop.naver.service;

import com.example.myselectshop.naver.dto.ItemDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j(topic = "NAVER API")
@Service
@RequiredArgsConstructor
public class NaverApiService {

    private final RestClient restClient;

    @Value("${naver.client.id}")
    private String naverClientId;

    @Value("${naver.client.secret}")
    private String naverClientSecret;

    public List<ItemDto> searchItems(String query) {
        String response = restClient
            .get()
            .uri(uriBuilder ->
                uriBuilder
                    .scheme("https")
                    .host("openapi.naver.com")
                    .path("/v1/search/shop.json")
                    .queryParam("display", 15)
                    .queryParam("query", query)
                    .build()
            )
            .header("X-Naver-Client-Id", naverClientId)
            .header("X-Naver-Client-Secret", naverClientSecret)
            .retrieve()
            .body(String.class);

        log.info("NAVER API 호출 성공");

        return fromJSONtoItems(response);
    }

    public List<ItemDto> fromJSONtoItems(String responseEntity) {
        JSONObject jsonObject = new JSONObject(responseEntity);
        JSONArray items = jsonObject.getJSONArray("items");
        List<ItemDto> itemDtoList = new ArrayList<>();

        for (Object item : items) {
            ItemDto itemDto = new ItemDto((JSONObject) item);
            itemDtoList.add(itemDto);
        }

        return itemDtoList;
    }
}
