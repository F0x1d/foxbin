package com.f0x1d.foxbin.model.response.note;

import com.f0x1d.foxbin.model.response.base.Response;
import org.springframework.http.HttpStatus;

public class DeletedNoteResponse extends Response {
    @Override
    public int code() {
        return HttpStatus.OK.value();
    }

    @Override
    public boolean ok() {
        return true;
    }
}
