package com.f0x1d.foxbin.model.response.note;

import com.f0x1d.foxbin.model.response.base.SuccessfulResponse;
import com.f0x1d.foxbin.model.response.note.usernote.UserNoteWithContent;

public class UserNoteResponse extends SuccessfulResponse {

    public UserNoteWithContent note;

    public UserNoteResponse(UserNoteWithContent note) {
        super();
        this.note = note;
    }
}
