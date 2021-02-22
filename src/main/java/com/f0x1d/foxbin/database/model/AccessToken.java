package com.f0x1d.foxbin.database.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

import java.util.Objects;

@Entity
public class AccessToken {

    @Id
    private long id;
    private String token;
    private ToOne<FoxBinUser> user;

    public AccessToken(long id, String token) {
        this.id = id;
        this.token = token;
    }

    public AccessToken(String token) {
        this.token = token;
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

    public void setUser(ToOne<FoxBinUser> user) {
        this.user = user;
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
