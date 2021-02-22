package com.f0x1d.foxbin.model.response.note;

import com.f0x1d.foxbin.model.response.base.Response;
import com.f0x1d.foxbin.model.response.note.usernote.UserNoteWithContent;
import org.springframework.http.HttpStatus;

public class UserNoteResponse extends Response {

    public UserNoteWithContent note;

    public UserNoteResponse(UserNoteWithContent note) {
        super();
        this.note = note;
    }

    @Override
    public int code() {
        return HttpStatus.OK.value();
    }

    @Override
    public boolean ok() {
        return true;
    }
}
