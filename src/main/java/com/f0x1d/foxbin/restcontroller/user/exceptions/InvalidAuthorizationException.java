package com.f0x1d.foxbin.restcontroller.user.exceptions;

public class InvalidAuthorizationException extends RuntimeException {

    public InvalidAuthorizationException() {
        super("Invalid authorization");
    }
}
