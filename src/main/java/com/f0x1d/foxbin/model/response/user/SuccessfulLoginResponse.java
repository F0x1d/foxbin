package com.f0x1d.foxbin.model.response.user;

import com.f0x1d.foxbin.model.response.base.Response;
import org.springframework.http.HttpStatus;

public class SuccessfulLoginResponse extends Response {

    public String accessToken;

    public SuccessfulLoginResponse(String token) {
        super();
        this.accessToken = token;
    }

    @Override
    public int code() {
        return HttpStatus.OK.value();
    }

    @Override
    public boolean ok() {
        return true;
    }
}
