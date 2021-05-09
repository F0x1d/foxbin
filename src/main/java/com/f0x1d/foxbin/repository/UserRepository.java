package com.f0x1d.foxbin.repository;

import com.f0x1d.foxbin.Constants;
import com.f0x1d.foxbin.database.ObjectBox;
import com.f0x1d.foxbin.database.model.AccessToken;
import com.f0x1d.foxbin.database.model.AccessToken_;
import com.f0x1d.foxbin.database.model.FoxBinUser;
import com.f0x1d.foxbin.database.model.FoxBinUser_;
import com.f0x1d.foxbin.restcontroller.user.exceptions.InvalidAuthorizationException;
import com.f0x1d.foxbin.utils.CryptUtils;
import io.objectbox.Box;
import io.objectbox.exception.UniqueViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    public List<FoxBinUser> getAllUsers() {
        return ObjectBox.get()
                .boxFor(FoxBinUser.class)
                .query()
                .build()
                .find();
    }

    public FoxBinUser foxBinUserByUsername(String username) {
        return ObjectBox.get()
                .boxFor(FoxBinUser.class)
                .query()
                .equal(FoxBinUser_.username, username)
                .build()
                .findFirst();
    }

    public FoxBinUser userFromAccessToken(String token) {
        Box<AccessToken> accessTokenBox = ObjectBox.get()
                .boxFor(AccessToken.class);

        AccessToken accessToken = accessTokenBox
                .query()
                .equal(AccessToken_.token, token)
                .build()
                .findFirst();

        if (accessToken == null)
            throw new InvalidAuthorizationException();

        accessTokenBox.put(accessToken.setLastUsedNow());
        return accessToken.getUser().getTarget();
    }

    public void addAccessToken(FoxBinUser foxBinUser, AccessToken accessToken) throws UniqueViolationException {
        foxBinUser.getAccessTokens().add(accessToken);

        ObjectBox.get()
                .boxFor(FoxBinUser.class)
                .put(foxBinUser);
    }

    public void removeAccessToken(AccessToken accessToken) {
        ObjectBox.get()
                .boxFor(AccessToken.class)
                .remove(accessToken);
    }

    public FoxBinUser createFoxBinUser(String username, String password) throws UniqueViolationException {
        FoxBinUser foxBinUser = FoxBinUser.create(username, CryptUtils.toMd5(password));

        ObjectBox.get()
                .boxFor(FoxBinUser.class)
                .put(foxBinUser);

        return foxBinUser;
    }

    public void createRootUser() {
        Box<FoxBinUser> foxBinUserBox = ObjectBox.get().boxFor(FoxBinUser.class);
        if (foxBinUserBox
                .query()
                .equal(FoxBinUser_.username, "root")
                .build()
                .findFirst() != null
        ) return;

        foxBinUserBox.put(FoxBinUser.create("root", "root"));
    }

    @Scheduled(initialDelay = Constants.ONE_MINUTE_MS, fixedRate = Constants.HALF_DAY_MS)
    public void checkTokens() {
        for (FoxBinUser foxBinUser : getAllUsers()) {
            for (AccessToken accessToken : foxBinUser.getAccessTokens()) {
                if (accessToken.getLastUsedAt() + Constants.NINETY_DAYS_MS < System.currentTimeMillis())
                    removeAccessToken(accessToken);
            }
        }
    }
}
