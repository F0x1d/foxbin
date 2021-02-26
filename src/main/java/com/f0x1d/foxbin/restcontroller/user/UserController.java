package com.f0x1d.foxbin.restcontroller.user;

import com.f0x1d.foxbin.model.requestbody.FoxBinUserRequestBody;
import com.f0x1d.foxbin.model.response.base.Response;
import com.f0x1d.foxbin.model.response.user.SuccessfulLoginResponse;
import com.f0x1d.foxbin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService mUserService;

    @PostMapping("/users/register")
    public Response register(@RequestBody FoxBinUserRequestBody foxBinUserRequestBody) {
        return new SuccessfulLoginResponse(mUserService.register(
                foxBinUserRequestBody.getUsername(),
                foxBinUserRequestBody.getPassword()
        ).getToken());
    }

    @PostMapping("/users/login")
    public Response login(@RequestBody FoxBinUserRequestBody foxBinUserRequestBody) {
        return new SuccessfulLoginResponse(mUserService.login(
                foxBinUserRequestBody.getUsername(),
                foxBinUserRequestBody.getPassword()
        ).getToken());
    }
}
