package com.f0x1d.foxbin.model.response.base;

import org.springframework.http.HttpStatus;

public class SuccessfulResponse extends Response {
    @Override
    public int code() {
        return HttpStatus.OK.value();
    }

    @Override
    public boolean ok() {
        return true;
    }
}
