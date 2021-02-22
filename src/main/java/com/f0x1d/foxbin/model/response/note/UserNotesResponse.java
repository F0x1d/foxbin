package com.f0x1d.foxbin.model.response.note;

import com.f0x1d.foxbin.database.model.FoxBinNote;
import com.f0x1d.foxbin.model.response.base.Response;
import com.f0x1d.foxbin.model.response.note.usernote.UserNote;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class UserNotesResponse extends Response {

    public List<UserNote> notes = new ArrayList<>();

    public UserNotesResponse(List<FoxBinNote> notes) {
        super();

        for (FoxBinNote note : notes) {
            this.notes.add(0, FoxBinNote.toUserNote(note, true));
        }
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
