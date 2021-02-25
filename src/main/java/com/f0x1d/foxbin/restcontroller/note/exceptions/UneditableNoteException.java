package com.f0x1d.foxbin.restcontroller.note.exceptions;

public class UneditableNoteException extends RuntimeException {

    public UneditableNoteException() {
        super("Seems that you don't have access to this note");
    }
}
