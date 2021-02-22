package com.f0x1d.foxbin.restcontroller.user.exceptions;

public class UsernameTakenException extends RuntimeException {

    public UsernameTakenException() {
        super("This username is already taken");
    }
}
