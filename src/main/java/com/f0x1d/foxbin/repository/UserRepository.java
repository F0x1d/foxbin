package com.f0x1d.foxbin.repository;

import com.f0x1d.foxbin.Constants;
import com.f0x1d.foxbin.database.ObjectBox;
import com.f0x1d.foxbin.database.model.AccessToken;
import com.f0x1d.foxbin.database.model.AccessToken_;
import com.f0x1d.foxbin.database.model.FoxBinUser;
import com.f0x1d.foxbin.database.model.FoxBinUser_;
import com.f0x1d.foxbin.restcontroller.user.exceptions.InvalidAuthorizationException;
import com.f0x1d.foxbin.utils.CryptUtils;
import com.f0x1d.foxbin.utils.DBUtils;
import io.objectbox.Box;
import io.objectbox.query.QueryBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    public List<FoxBinUser> getAllUsers() {
        return ObjectBox.get().callInReadTx(() -> DBUtils.boxForUsers().getAll());
    }

    public FoxBinUser foxBinUserByUsername(String username) {
        return ObjectBox.get().callInReadTx(() -> DBUtils.findFirst(
                DBUtils
                        .boxForUsers()
                        .query()
                        .equal(FoxBinUser_.username, username, QueryBuilder.StringOrder.CASE_SENSITIVE)
                        .build()
        ));
    }

    public FoxBinUser userFromAccessToken(String token) {
        return ObjectBox.get().callInTxNoException(() -> {
            Box<AccessToken> accessTokenBox = DBUtils.boxForTokens();

            AccessToken accessToken = DBUtils.findFirst(
                    accessTokenBox
                            .query()
                            .equal(AccessToken_.token, token, QueryBuilder.StringOrder.CASE_SENSITIVE)
                            .build()
            );

            if (accessToken == null)
                throw new InvalidAuthorizationException();

            accessTokenBox.put(accessToken.setLastUsedNow());
            return accessToken.getUser().getTarget();
        });
    }

    public void addAccessToken(FoxBinUser foxBinUser, AccessToken accessToken) {
        ObjectBox.get().runInTx(() -> {
            foxBinUser.getAccessTokens().add(accessToken);

            DBUtils
                    .boxForUsers()
                    .put(foxBinUser);
        });
    }

    public FoxBinUser createFoxBinUser(String username, String password) throws Exception {
        return ObjectBox.get().callInTx(() -> {
            FoxBinUser foxBinUser = FoxBinUser.create(username, CryptUtils.toMd5(password));

            DBUtils
                    .boxForUsers()
                    .put(foxBinUser);

            return foxBinUser;
        });
    }

    public void createRootUser() {
        ObjectBox.get().runInTx(() -> {
            Box<FoxBinUser> foxBinUserBox = DBUtils.boxForUsers();
            if (DBUtils.findFirst(
                    foxBinUserBox
                            .query()
                            .equal(FoxBinUser_.username, "root", QueryBuilder.StringOrder.CASE_SENSITIVE)
                            .build()
            ) != null) return;

            foxBinUserBox.put(FoxBinUser.create("root", "root"));
        });
    }

    @Scheduled(initialDelay = Constants.ONE_MINUTE_MS, fixedRate = Constants.HALF_DAY_MS)
    public void checkTokens() {
        ObjectBox.get().runInTx(() -> {
            List<AccessToken> tokesToRemove = new ArrayList<>();

            for (FoxBinUser foxBinUser : getAllUsers()) {
                for (AccessToken accessToken : foxBinUser.getAccessTokens()) {
                    if (accessToken.getLastUsedAt() + Constants.NINETY_DAYS_MS < System.currentTimeMillis())
                        tokesToRemove.add(accessToken);
                }
            }

            DBUtils
                    .boxForTokens()
                    .remove(tokesToRemove);
        });
    }
}
