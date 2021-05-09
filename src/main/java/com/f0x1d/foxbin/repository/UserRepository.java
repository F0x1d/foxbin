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

    public boolean tokenExists(String accessToken) {
        return ObjectBox.get()
                .boxFor(AccessToken.class)
                .query()
                .equal(AccessToken_.token, accessToken)
                .build()
                .findFirst() != null;
    }

    public void addAccessToken(FoxBinUser foxBinUser, AccessToken accessToken) {
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

    public void createFoxBinUser(String username, String password, AccessToken accessToken) {
        ObjectBox.get()
                .boxFor(FoxBinUser.class)
                .put(FoxBinUser.create(
                        username,
                        CryptUtils.toMd5(password),
                        accessToken
                ));
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

    public void checkTokens() {
        for (FoxBinUser foxBinUser : getAllUsers()) {
            for (AccessToken accessToken : foxBinUser.getAccessTokens()) {
                if (accessToken.getLastUsedAt() + Constants.NINETY_DAYS_MS < System.currentTimeMillis())
                    removeAccessToken(accessToken);
            }
        }
    }
}
