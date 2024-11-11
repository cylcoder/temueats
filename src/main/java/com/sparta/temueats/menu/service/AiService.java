package com.sparta.temueats.menu.service;

import lombok.RequiredArgsConstructor;
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

    @Value("${ai.api.url}")
    private String url;

    public static final String CONSTRAINT = "답변을 최대한 간결하게 50자 이하로";

    private final RestTemplate restTemplate;

    public String request(String req) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String requestBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + req + CONSTRAINT + "\"}]}]}";
        System.out.println("requestBody = " + requestBody);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }

}
