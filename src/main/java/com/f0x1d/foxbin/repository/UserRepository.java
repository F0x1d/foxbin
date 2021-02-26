package com.f0x1d.foxbin.repository;

import com.f0x1d.foxbin.database.ObjectBox;
import com.f0x1d.foxbin.database.model.AccessToken;
import com.f0x1d.foxbin.database.model.AccessToken_;
import com.f0x1d.foxbin.database.model.FoxBinUser;
import com.f0x1d.foxbin.database.model.FoxBinUser_;
import com.f0x1d.foxbin.utils.CryptUtils;
import io.objectbox.Box;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    public FoxBinUser foxBinUserByUsername(String username) {
        return ObjectBox.get()
                .boxFor(FoxBinUser.class)
                .query()
                .equal(FoxBinUser_.username, username)
                .build()
                .findFirst();
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
}
