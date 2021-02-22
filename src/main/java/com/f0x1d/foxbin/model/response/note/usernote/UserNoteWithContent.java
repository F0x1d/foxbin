package com.f0x1d.foxbin.model.response.note.usernote;

import java.util.Date;

public class UserNoteWithContent extends UserNote {

    public String content;

    public static UserNoteWithContent create(String content, String slug, Date date, boolean editable) {
        UserNoteWithContent userNoteWithContent = new UserNoteWithContent();
        userNoteWithContent.setContent(content);
        userNoteWithContent.setSlug(slug);
        userNoteWithContent.setDate(date.getTime());
        userNoteWithContent.setEditable(editable);

        return userNoteWithContent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
