package com.f0x1d.foxbin.service;

import com.f0x1d.foxbin.database.model.AccessToken;
import com.f0x1d.foxbin.database.model.FoxBinUser;
import com.f0x1d.foxbin.repository.UserRepository;
import com.f0x1d.foxbin.restcontroller.user.exceptions.InvalidLoginOrPasswordException;
import com.f0x1d.foxbin.restcontroller.user.exceptions.SomethingIsEmptyException;
import com.f0x1d.foxbin.restcontroller.user.exceptions.UsernameTakenException;
import com.f0x1d.foxbin.utils.CryptUtils;
import com.f0x1d.foxbin.utils.DBUtils;
import com.f0x1d.foxbin.utils.RandomStringGenerator;
import io.objectbox.exception.UniqueViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserService {

    @Autowired
    private UserRepository mUserRepository;

    @Autowired
    private RandomStringGenerator mRandomStringGenerator;

    public AccessToken register(String username, String password) {
        if (emptyRequestBody(username, password))
            throw new SomethingIsEmptyException();

        FoxBinUser user;
        try {
            user = mUserRepository.createFoxBinUser(username, password);
        } catch (Exception e) {
            throw new UsernameTakenException();
        }

        AccessToken accessToken = addTokenToUser(user);
        DBUtils.closeThreadResources();
        return accessToken;
    }

    public AccessToken login(String username, String password) {
        if (emptyRequestBody(username, password))
            throw new SomethingIsEmptyException();

        FoxBinUser user = mUserRepository.foxBinUserByUsername(username);
        if (user == null || !user.getPassword().equals(CryptUtils.toMd5(password)) || username.equals("root"))
            throw new InvalidLoginOrPasswordException();

        AccessToken accessToken = addTokenToUser(user);
        DBUtils.closeThreadResources();
        return accessToken;
    }

    private AccessToken addTokenToUser(FoxBinUser user) {
        try {
            AccessToken accessToken = new AccessToken(generateToken());
            mUserRepository.addAccessToken(user, accessToken);

            return accessToken;
        } catch (UniqueViolationException e) {
            return addTokenToUser(user);
        }
    }

    private String generateToken() {
        return mRandomStringGenerator.nextToken();
    }

    private boolean emptyRequestBody(String... params) {
        return Arrays.stream(params).anyMatch(String::isEmpty);
    }
}
