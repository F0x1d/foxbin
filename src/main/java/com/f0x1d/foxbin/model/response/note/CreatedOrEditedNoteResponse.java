package com.f0x1d.foxbin.model.response.note;

import com.f0x1d.foxbin.model.response.base.SuccessfulResponse;

public class CreatedOrEditedNoteResponse extends SuccessfulResponse {

    public String slug;

    public CreatedOrEditedNoteResponse(String slug) {
        super();
        this.slug = slug;
    }
}
