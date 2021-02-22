package com.f0x1d.foxbin.repository;

import com.f0x1d.foxbin.database.ObjectBox;
import com.f0x1d.foxbin.database.model.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoteRepository {

    public void createNote(String content, String slug, FoxBinUser user) {
        ObjectBox.get()
                .boxFor(FoxBinNote.class)
                .put(FoxBinNote.createNote(
                        content,
                        slug,
                        user
                ));
    }

    public List<FoxBinNote> userNotes(String accessToken) {
        return userFromAccessToken(accessToken)
                .getNotes();
    }

    public FoxBinUser userFromAccessToken(String token) {
        return ObjectBox.get()
                .boxFor(AccessToken.class)
                .query()
                .equal(AccessToken_.token, token)
                .build()
                .findFirst()
                .getUser()
                .getTarget();
    }

    public FoxBinNote noteFromSlug(String slug) {
        return ObjectBox.get()
                .boxFor(FoxBinNote.class)
                .query()
                .equal(FoxBinNote_.slug, slug)
                .build()
                .findFirst();
    }
}
