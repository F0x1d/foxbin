package com.f0x1d.foxbin.service;

import com.f0x1d.foxbin.database.model.AccessToken;
import com.f0x1d.foxbin.database.model.FoxBinUser;
import com.f0x1d.foxbin.repository.UserRepository;
import com.f0x1d.foxbin.restcontroller.user.exceptions.InvalidLoginOrPasswordException;
import com.f0x1d.foxbin.restcontroller.user.exceptions.SomethingIsEmptyException;
import com.f0x1d.foxbin.restcontroller.user.exceptions.UsernameTakenException;
import com.f0x1d.foxbin.utils.CryptUtils;
import com.f0x1d.foxbin.utils.RandomStringGenerator;
import io.objectbox.exception.UniqueViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository mUserRepository;

    @Autowired
    private RandomStringGenerator mRandomStringGenerator;

    public AccessToken register(String username, String password) {
        if (checkRequestBody(username, password))
            throw new SomethingIsEmptyException();

        FoxBinUser foxBinUser = mUserRepository.foxBinUserByUsername(username);
        if (foxBinUser != null)
            throw new UsernameTakenException();

        FoxBinUser user;
        try {
            user = mUserRepository.createFoxBinUser(username, password);
        } catch (Exception e) {
            throw new UsernameTakenException();
        }

        return addTokenToUser(user);
    }

    public AccessToken login(String username, String password) {
        if (checkRequestBody(username, password))
            throw new SomethingIsEmptyException();

        FoxBinUser foxBinUser = mUserRepository.foxBinUserByUsername(username);
        if (foxBinUser == null || !foxBinUser.getPassword().equals(CryptUtils.toMd5(password)) || username.equals("root"))
            throw new InvalidLoginOrPasswordException();

        return addTokenToUser(foxBinUser);
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

    private boolean checkRequestBody(String username, String password) {
        return username.isEmpty() || password.isEmpty();
    }
}
