package com.f0x1d.foxbin.repository;

import com.f0x1d.foxbin.database.ObjectBox;
import com.f0x1d.foxbin.database.model.FoxBinNote;
import com.f0x1d.foxbin.database.model.FoxBinNote_;
import com.f0x1d.foxbin.database.model.FoxBinUser;
import com.f0x1d.foxbin.database.model.FoxBinUser_;
import org.springframework.stereotype.Repository;

@Repository
public class NoteRepository {

    private FoxBinUser mRootUser;

    public void createNote(String content, String slug, FoxBinUser user) {
        ObjectBox.get()
                .boxFor(FoxBinNote.class)
                .put(FoxBinNote.createNote(
                        content,
                        slug,
                        user == null ? rootUser() : user
                ));
    }

    public void editNote(String content, FoxBinNote foxBinNote) {
        foxBinNote.setContent(content);

        ObjectBox.get()
                .boxFor(FoxBinNote.class)
                .put(foxBinNote);
    }

    public FoxBinNote noteFromSlug(String slug) {
        return ObjectBox.get()
                .boxFor(FoxBinNote.class)
                .query()
                .equal(FoxBinNote_.slug, slug)
                .build()
                .findFirst();
    }

    private FoxBinUser rootUser() {
        if (mRootUser == null) {
            return mRootUser = ObjectBox.get()
                    .boxFor(FoxBinUser.class)
                    .query()
                    .equal(FoxBinUser_.username, "root")
                    .build()
                    .findFirst();
        } else return mRootUser;
    }
}
