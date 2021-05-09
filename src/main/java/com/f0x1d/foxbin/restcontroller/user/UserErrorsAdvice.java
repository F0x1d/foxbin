package com.f0x1d.foxbin.restcontroller.user;

import com.f0x1d.foxbin.model.response.ErrorResponse;
import com.f0x1d.foxbin.restcontroller.note.exceptions.EmptyContentException;
import com.f0x1d.foxbin.restcontroller.user.exceptions.InvalidAuthorizationException;
import com.f0x1d.foxbin.restcontroller.user.exceptions.InvalidLoginOrPasswordException;
import com.f0x1d.foxbin.restcontroller.user.exceptions.SomethingIsEmptyException;
import com.f0x1d.foxbin.restcontroller.user.exceptions.UsernameTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserErrorsAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NullPointerException.class)
    public ErrorResponse nullPointer(NullPointerException nullPointerException) {
        nullPointerException.printStackTrace();
        return new ErrorResponse("Seems that something is null");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SomethingIsEmptyException.class)
    public ErrorResponse somethingIsEmpty(SomethingIsEmptyException somethingIsEmptyException) {
        return new ErrorResponse(somethingIsEmptyException.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidLoginOrPasswordException.class)
    public ErrorResponse invalidLoginOrPassword(InvalidLoginOrPasswordException invalidLoginOrPasswordException) {
        return new ErrorResponse(invalidLoginOrPasswordException.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UsernameTakenException.class)
    public ErrorResponse usernameTakenException(UsernameTakenException usernameTakenException) {
        return new ErrorResponse(usernameTakenException.getMessage(), HttpStatus.CONFLICT);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmptyContentException.class)
    public ErrorResponse emptyContent(EmptyContentException emptyContentException) {
        return new ErrorResponse(emptyContentException.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidAuthorizationException.class)
    public ErrorResponse invalidAuthorization(InvalidAuthorizationException invalidAuthorizationException) {
        return new ErrorResponse(invalidAuthorizationException.getMessage(), HttpStatus.UNAUTHORIZED);
    }

}
