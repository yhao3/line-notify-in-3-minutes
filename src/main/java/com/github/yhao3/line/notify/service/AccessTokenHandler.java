package com.github.yhao3.line.notify.service;

import java.util.Optional;

public interface AccessTokenHandler {
    void saveAccessToken(String accessToken);
    Optional<String> getAccessToken();
    Boolean isAccessTokenExist();
    void deleteAccessToken();
}
