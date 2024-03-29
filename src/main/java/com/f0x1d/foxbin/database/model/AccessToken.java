package com.f0x1d.foxbin.database.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Unique;
import io.objectbox.relation.ToOne;

import java.util.Objects;

@Entity
public class AccessToken {

    @Id
    private long id;
    @Unique
    private String token;
    private ToOne<FoxBinUser> user;

    private long lastUsedAt;

    public AccessToken(long id, String token, long lastUsedAt) {
        this.id = id;
        this.token = token;
        this.lastUsedAt = lastUsedAt;
    }

    public AccessToken(String token) {
        this.token = token;
        this.lastUsedAt = System.currentTimeMillis();
    }

    public AccessToken() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ToOne<FoxBinUser> getUser() {
        return user;
    }

    public long getLastUsedAt() {
        return lastUsedAt;
    }

    public void setLastUsedAt(long lastUsedAt) {
        this.lastUsedAt = lastUsedAt;
    }

    public AccessToken setLastUsedNow() {
        this.lastUsedAt = System.currentTimeMillis();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccessToken that = (AccessToken) o;
        return token.equals(that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
