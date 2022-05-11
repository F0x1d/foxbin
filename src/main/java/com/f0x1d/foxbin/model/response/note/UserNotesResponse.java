package com.f0x1d.foxbin.model.response.note;

import com.f0x1d.foxbin.database.model.FoxBinNote;
import com.f0x1d.foxbin.model.response.base.SuccessfulResponse;
import com.f0x1d.foxbin.model.response.note.usernote.UserNote;

import java.util.ArrayList;
import java.util.List;

public class UserNotesResponse extends SuccessfulResponse {

    public List<UserNote> notes = new ArrayList<>();

    public UserNotesResponse(List<FoxBinNote> notes) {
        super();

        for (FoxBinNote note : notes) {
            this.notes.add(0, FoxBinNote.toUserNote(note, true));
        }
    }
}
