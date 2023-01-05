package com.f0x1d.foxbin.service;

import com.f0x1d.foxbin.database.ObjectBox;
import com.f0x1d.foxbin.database.model.AccessToken;
import com.f0x1d.foxbin.database.model.FoxBinUser;
import com.f0x1d.foxbin.restcontroller.user.exceptions.InvalidLoginOrPasswordException;
import com.f0x1d.foxbin.restcontroller.user.exceptions.SomethingIsEmptyException;
import com.f0x1d.foxbin.restcontroller.user.exceptions.UsernameTakenException;
import com.f0x1d.foxbin.service.base.BaseService;
import com.f0x1d.foxbin.utils.CryptUtils;
import com.f0x1d.foxbin.utils.DBUtils;
import io.objectbox.exception.UniqueViolationException;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService {

    public AccessToken register(String username, String password) {
        if (anyEmpty(username, password))
            throw new SomethingIsEmptyException();

        AccessToken resultToken = ObjectBox.get().callInTxNoException(() -> {
            FoxBinUser user;
            try {
                user = mUserRepository.createFoxBinUser(username, password);
            } catch (Exception e) {
                throw new UsernameTakenException();
            }

            return addTokenToUser(user);
        });

        DBUtils.closeThreadResources();
        return resultToken;
    }

    public AccessToken login(String username, String password) {
        if (anyEmpty(username, password))
            throw new SomethingIsEmptyException();

        AccessToken resultToken = ObjectBox.get().callInTxNoException(() -> {
            FoxBinUser user = mUserRepository.foxBinUserByUsername(username);
            if (user == null || !user.getPassword().equals(CryptUtils.toMd5(password)) || username.equals("root"))
                throw new InvalidLoginOrPasswordException();

            return addTokenToUser(user);
        });

        DBUtils.closeThreadResources();
        return resultToken;
    }

    private AccessToken addTokenToUser(FoxBinUser user) {
        try {
            AccessToken accessToken = new AccessToken(mRandomStringGenerator.nextToken());
            mUserRepository.addAccessToken(user, accessToken);

            return accessToken;
        } catch (UniqueViolationException e) {
            return addTokenToUser(user);
        }
    }
}
