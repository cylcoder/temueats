package com.sparta.temueats.menu.service;

import com.sparta.temueats.menu.entity.P_aiLog;
import com.sparta.temueats.menu.repository.AiRepository;
import com.sparta.temueats.user.entity.P_user;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AiService {

//    @Value("${ai.api.url}")
    private String url;

    public static final String CONSTRAINT = "답변을 최대한 간결하게 50자 이하로";

    private final RestTemplate restTemplate;
    private final AiRepository aiRepository;

    public String request(String request, P_user user) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        String requestBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + request + CONSTRAINT + "\"}]}]}";
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        String responseBody = response.getBody();
        JSONObject jsonResponse = new JSONObject(responseBody);
        JSONArray candidates = jsonResponse.getJSONArray("candidates");
        JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
        JSONArray parts = content.getJSONArray("parts");
        String text = parts.getJSONObject(0).getString("text");

        P_aiLog aiLog = P_aiLog.builder()
                .request(request)
                .response(text)
                .build();
        aiLog.setCreatedBy(user.getNickname());
        aiRepository.save(aiLog);

        return text;
    }

}
