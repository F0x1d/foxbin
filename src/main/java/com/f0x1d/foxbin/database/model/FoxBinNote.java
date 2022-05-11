package com.f0x1d.foxbin.database.model;

import com.f0x1d.foxbin.model.response.note.usernote.UserNote;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

import java.util.Date;

@Entity
public class FoxBinNote {

    @Id
    private long id;

    private String slug;
    private String content;
    private Date date;
    private long deleteAfter;

    private ToOne<FoxBinUser> user;

    public static FoxBinNote createNote(String content, String slug, long deleteAfter, FoxBinUser foxBinUser) {
        FoxBinNote foxBinNote = new FoxBinNote();
        foxBinNote.setContent(content);
        foxBinNote.setSlug(slug);
        foxBinNote.setDate(new Date());
        foxBinNote.setDeleteAfter(deleteAfter);
        if (foxBinUser != null) foxBinNote.user.setTarget(foxBinUser);

        return foxBinNote;
    }

    public static UserNote toUserNote(FoxBinNote foxBinNote, boolean editable) {
        UserNote userNote = new UserNote();
        userNote.setSlug(foxBinNote.slug);
        userNote.setDate(foxBinNote.date.getTime());
        userNote.setEditable(editable);

        return userNote;
    }

    public FoxBinNote() {
    }

    public FoxBinNote(long id, String slug, String content, Date date) {
        this.id = id;
        this.slug = slug;
        this.content = content;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getDeleteAfter() {
        return deleteAfter;
    }

    public void setDeleteAfter(long deleteAfter) {
        this.deleteAfter = deleteAfter;
    }

    public ToOne<FoxBinUser> getUser() {
        return user;
    }
}
