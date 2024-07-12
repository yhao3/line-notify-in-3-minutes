package com.github.yhao3.line.notify.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineNotifyToken {

    /**
     * An access token for authentication. Used for calling the notification API.
     * This access token has no expiration date.
     */
    @JsonProperty("access_token")
    private String accessToken;
}
