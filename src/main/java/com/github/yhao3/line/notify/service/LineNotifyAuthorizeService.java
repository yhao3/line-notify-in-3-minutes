package com.github.yhao3.line.notify.service;

import ch.qos.logback.core.Context;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class LineNotifyAuthorizeService {

    @Value("${line-notify.client-id}")
    private String clientId;

    @Value("${line-notify.client-secret}")
    private String clientSecret;

    private final Environment env;
    private final AccessTokenHandler accessTokenHandler;

    public void exchangeToken(String code) {
        // Get access token:
        LineNotifyToken lineNotifyToken = exchangeTokenByAuthCode(code);
        // save access token as a file
        accessTokenHandler.saveAccessToken(lineNotifyToken.getAccessToken());
    }

    private LineNotifyToken exchangeTokenByAuthCode(String code) {

        String serverPort = env.getProperty("server.port");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("redirect_uri", String.format("http://localhost:%s/api/line-notify/exchange-token", serverPort));
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        LineNotifyToken lineNotifyToken = restTemplate.postForObject("https://notify-bot.line.me/oauth/token", entity, LineNotifyToken.class);

        if (lineNotifyToken == null) {
            throw new RuntimeException("Failed to get access token");
        }

        return lineNotifyToken;
    }

    public Boolean isAuthorized() {
        return accessTokenHandler.isAccessTokenExist();
    }

    public void revoke() {
        accessTokenHandler.getAccessToken().ifPresent(accessToken -> {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);

            HttpEntity<Context> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForObject("https://notify-api.line.me/api/revoke", entity, Void.class);

            // delete access token
            accessTokenHandler.deleteAccessToken();
        });
    }
}
