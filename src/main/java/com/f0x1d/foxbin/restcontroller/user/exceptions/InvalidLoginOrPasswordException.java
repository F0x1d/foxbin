package com.f0x1d.foxbin.restcontroller.user.exceptions;

public class InvalidLoginOrPasswordException extends RuntimeException {

    public InvalidLoginOrPasswordException() {
        super("Invalid login or password");
    }
}
