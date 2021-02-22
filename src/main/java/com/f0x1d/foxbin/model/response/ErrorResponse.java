package com.f0x1d.foxbin.model.response;

import com.f0x1d.foxbin.model.response.base.Response;
import org.springframework.http.HttpStatus;

public class ErrorResponse extends Response {

    public String error;

    public ErrorResponse(String error) {
        super();
        this.error = error;
    }

    public ErrorResponse(String error, HttpStatus httpStatus) {
        super();
        this.error = error;
        this.code = httpStatus.value();
    }

    @Override
    public int code() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public boolean ok() {
        return false;
    }
}
