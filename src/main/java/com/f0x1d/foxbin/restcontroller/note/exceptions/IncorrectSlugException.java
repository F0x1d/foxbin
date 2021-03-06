package com.f0x1d.foxbin.restcontroller.note.exceptions;

public class IncorrectSlugException extends RuntimeException {

    public IncorrectSlugException() {
        super("Slug must contain only A-Z, a-z, 0-9 characters and be 5-64 characters in length");
    }
}
