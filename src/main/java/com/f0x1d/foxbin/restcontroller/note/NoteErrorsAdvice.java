package com.f0x1d.foxbin.restcontroller.note;

import com.f0x1d.foxbin.model.response.ErrorResponse;
import com.f0x1d.foxbin.restcontroller.note.exceptions.NoSuchNoteException;
import com.f0x1d.foxbin.restcontroller.note.exceptions.SlugTakenException;
import com.f0x1d.foxbin.restcontroller.note.exceptions.UneditableNoteException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class NoteErrorsAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(SlugTakenException.class)
    public ErrorResponse slugTaken(SlugTakenException slugTakenException) {
        return new ErrorResponse(slugTakenException.getMessage(), HttpStatus.CONFLICT);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchNoteException.class)
    public ErrorResponse noSuchNote(NoSuchNoteException noSuchNoteException) {
        return new ErrorResponse(noSuchNoteException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UneditableNoteException.class)
    public ErrorResponse uneditableNote(UneditableNoteException uneditableNoteException) {
        return new ErrorResponse(uneditableNoteException.getMessage());
    }
}
