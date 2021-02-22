package com.f0x1d.foxbin.restcontroller.user;

import com.f0x1d.foxbin.model.response.ErrorResponse;
import com.f0x1d.foxbin.restcontroller.user.exceptions.InvalidLoginOrPasswordException;
import com.f0x1d.foxbin.restcontroller.user.exceptions.SomethingIsEmptyException;
import com.f0x1d.foxbin.restcontroller.user.exceptions.UsernameTakenException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class UserErrorsAdvice {

    @ResponseBody
    @ExceptionHandler(NullPointerException.class)
    public ErrorResponse nullPointer(NullPointerException nullPointerException) {
        nullPointerException.printStackTrace();
        return new ErrorResponse("Seems that something is null");
    }

    @ResponseBody
    @ExceptionHandler(SomethingIsEmptyException.class)
    public ErrorResponse somethingIsEmpty(SomethingIsEmptyException somethingIsEmptyException) {
        return new ErrorResponse(somethingIsEmptyException.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(InvalidLoginOrPasswordException.class)
    public ErrorResponse invalidLoginOrPassword(InvalidLoginOrPasswordException invalidLoginOrPasswordException) {
        return new ErrorResponse(invalidLoginOrPasswordException.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(UsernameTakenException.class)
    public ErrorResponse usernameTakenException(UsernameTakenException usernameTakenException) {
        return new ErrorResponse(usernameTakenException.getMessage(), HttpStatus.CONFLICT);
    }

}
