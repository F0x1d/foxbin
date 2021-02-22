package com.f0x1d.foxbin.restcontroller.user.exceptions;

public class SomethingIsEmptyException extends RuntimeException {

    public SomethingIsEmptyException() {
        super("Something is empty");
    }
}
