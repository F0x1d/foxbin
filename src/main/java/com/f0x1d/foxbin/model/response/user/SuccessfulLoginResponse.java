package com.f0x1d.foxbin.model.response.user;

import com.f0x1d.foxbin.model.response.base.SuccessfulResponse;

public class SuccessfulLoginResponse extends SuccessfulResponse {

    public String accessToken;

    public SuccessfulLoginResponse(String token) {
        super();
        this.accessToken = token;
    }
}
