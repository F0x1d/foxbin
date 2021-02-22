package com.f0x1d.foxbin.restcontroller.note.exceptions;

public class NoSuchNoteException extends RuntimeException {

    public NoSuchNoteException() {
        super("There is no note with provided slug");
    }
}
