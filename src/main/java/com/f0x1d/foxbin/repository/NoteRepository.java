package com.f0x1d.foxbin.repository;

import com.f0x1d.foxbin.Constants;
import com.f0x1d.foxbin.database.model.FoxBinNote;
import com.f0x1d.foxbin.database.model.FoxBinNote_;
import com.f0x1d.foxbin.database.model.FoxBinUser;
import com.f0x1d.foxbin.database.model.FoxBinUser_;
import com.f0x1d.foxbin.utils.DBUtils;
import io.objectbox.Box;
import io.objectbox.query.QueryBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NoteRepository {

    private FoxBinUser mRootUser;

    public List<FoxBinNote> getAll() {
        return DBUtils.boxForNotes().getAll();
    }

    public void createNote(String content, String slug, long deleteAfter, FoxBinUser user) {
        DBUtils
                .boxForNotes()
                .put(FoxBinNote.createNote(
                        content,
                        slug,
                        deleteAfter,
                        user == null ? rootUser() : user
                ));
    }

    public void deleteNote(FoxBinNote foxBinNote) {
        DBUtils
                .boxForNotes()
                .remove(foxBinNote);
    }

    public void editNote(String content, FoxBinNote foxBinNote) {
        foxBinNote.setContent(content);

        DBUtils
                .boxForNotes()
                .put(foxBinNote);
    }

    public FoxBinNote noteFromSlug(String slug) {
        return DBUtils.findFirst(
                DBUtils
                        .boxForNotes()
                        .query()
                        .equal(FoxBinNote_.slug, slug, QueryBuilder.StringOrder.CASE_SENSITIVE)
                        .build()
        );
    }

    private FoxBinUser rootUser() {
        return mRootUser == null ? mRootUser = DBUtils.findFirst(
                DBUtils
                        .boxForUsers()
                        .query()
                        .equal(FoxBinUser_.username, "root", QueryBuilder.StringOrder.CASE_SENSITIVE)
                        .build()
        ) : mRootUser;
    }

    @Scheduled(initialDelay = Constants.ONE_MINUTE_MS, fixedRate = Constants.THREE_MINUTES_MS)
    public void checkExpiration() {
        Box<FoxBinNote> noteBox = DBUtils.boxForNotes();

        List<FoxBinNote> expiringNotes = DBUtils.find(
                noteBox
                        .query()
                        .greater(FoxBinNote_.deleteAfter, 0)
                        .build()
        );

        for (FoxBinNote expiringNote : expiringNotes) {
            if (expiringNote.getDate().getTime() + expiringNote.getDeleteAfter() <= System.currentTimeMillis())
                noteBox.remove(expiringNote);
        }
    }
}
