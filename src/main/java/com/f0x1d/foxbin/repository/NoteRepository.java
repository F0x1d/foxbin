package com.f0x1d.foxbin.repository;

import com.f0x1d.foxbin.Constants;
import com.f0x1d.foxbin.database.ObjectBox;
import com.f0x1d.foxbin.database.model.FoxBinNote;
import com.f0x1d.foxbin.database.model.FoxBinNote_;
import com.f0x1d.foxbin.database.model.FoxBinUser;
import com.f0x1d.foxbin.database.model.FoxBinUser_;
import io.objectbox.Box;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoteRepository {

    private FoxBinUser mRootUser;

    public void createNote(String content, String slug, long deleteAfter, FoxBinUser user) {
        ObjectBox.get()
                .boxFor(FoxBinNote.class)
                .put(FoxBinNote.createNote(
                        content,
                        slug,
                        deleteAfter,
                        user == null ? rootUser() : user
                ));
    }

    public void deleteNote(FoxBinNote foxBinNote) {
        ObjectBox.get()
                .boxFor(FoxBinNote.class)
                .remove(foxBinNote);
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

    @Scheduled(initialDelay = Constants.ONE_MINUTE_MS, fixedRate = Constants.THREE_MINUTES_MS)
    public void checkExpiration() {
        Box<FoxBinNote> noteBox = ObjectBox.get().boxFor(FoxBinNote.class);

        List<FoxBinNote> expiringNotes = noteBox
                .query()
                .greater(FoxBinNote_.deleteAfter, 0)
                .build()
                .find();

        for (FoxBinNote expiringNote : expiringNotes) {
            if (expiringNote.getDate().getTime() + expiringNote.getDeleteAfter() <= System.currentTimeMillis())
                noteBox.remove(expiringNote);
        }
    }
}
