package com.f0x1d.foxbin.database.model;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Unique;
import io.objectbox.relation.ToMany;

import java.util.List;
import java.util.Objects;

@Entity
public class FoxBinUser {

    @Id
    private long id;

    @Unique
    private String username;
    private String password;

    @Backlink(to = "user")
    private ToMany<FoxBinNote> notes;
    @Backlink(to = "user")
    private ToMany<AccessToken> accessTokens;

    public static FoxBinUser create(String username, String password, AccessToken accessToken) {
        FoxBinUser foxBinUser = new FoxBinUser();
        foxBinUser.setUsername(username);
        foxBinUser.setPassword(password);
        foxBinUser.getAccessTokens().add(accessToken);

        return foxBinUser;
    }

    public FoxBinUser() {
    }

    public FoxBinUser(long id, String username, String password, ToMany<AccessToken> accessTokens, ToMany<FoxBinNote> notes) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.accessTokens = accessTokens;
        this.notes = notes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ToMany<AccessToken> getAccessTokens() {
        return accessTokens;
    }

    public void setAccessTokens(ToMany<AccessToken> accessTokens) {
        this.accessTokens = accessTokens;
    }

    public List<FoxBinNote> getNotes() {
        return notes;
    }

    public void setNotes(ToMany<FoxBinNote> notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoxBinUser that = (FoxBinUser) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, notes, accessTokens);
    }
}
