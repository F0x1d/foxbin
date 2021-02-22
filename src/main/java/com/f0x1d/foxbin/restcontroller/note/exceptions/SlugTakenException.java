package com.f0x1d.foxbin.restcontroller.note.exceptions;

public class SlugTakenException extends RuntimeException {

    public SlugTakenException() {
        super("This slug is already in use");
    }
}
