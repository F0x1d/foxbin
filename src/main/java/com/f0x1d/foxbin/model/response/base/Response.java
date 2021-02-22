package com.f0x1d.foxbin.model.response.base;

public abstract class Response {

    public int code;
    public boolean ok;

    public Response() {
        this.code = code();
        this.ok = ok();
    }

    public abstract int code();
    public abstract boolean ok();
}
