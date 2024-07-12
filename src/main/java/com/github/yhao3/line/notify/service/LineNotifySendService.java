package com.github.yhao3.line.notify.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class LineNotifySendService {

    private final AccessTokenHandler accessTokenHandler;

    public void send(String message) {

        // 1. Get token from file
        String accessToken = accessTokenHandler.getAccessToken()
                .orElseThrow(() -> new RuntimeException("Access token not found, please authorize first"));

        // 2. Send message
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("message", message);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.postForEntity("https://notify-api.line.me/api/notify", entity, Map.class);
        } catch (Exception e) {
            // This access token has no expiration date, so it will not be expired.
            throw new RuntimeException("Failed to send notify, please exchange token again");
        }
    }

}
