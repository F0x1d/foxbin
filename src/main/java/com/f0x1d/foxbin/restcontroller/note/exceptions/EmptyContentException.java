package com.f0x1d.foxbin.restcontroller.note.exceptions;

public class EmptyContentException extends RuntimeException {

    public EmptyContentException() {
        super("Content is empty");
    }
}
