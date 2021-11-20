package com.f0x1d.foxbin.restcontroller.note;

import com.f0x1d.foxbin.model.requestbody.CreateOrEditNoteRequestBody;
import com.f0x1d.foxbin.model.response.base.Response;
import com.f0x1d.foxbin.model.response.note.CreatedOrEditedNoteResponse;
import com.f0x1d.foxbin.model.response.note.DeletedNoteResponse;
import com.f0x1d.foxbin.model.response.note.UserNoteResponse;
import com.f0x1d.foxbin.model.response.note.UserNotesResponse;
import com.f0x1d.foxbin.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class NoteRestController {

    @Autowired
    private NoteService mNoteService;

    @PostMapping("/create")
    public Response createNote(@RequestBody CreateOrEditNoteRequestBody createOrEditNoteRequestBody) {
        return new CreatedOrEditedNoteResponse(mNoteService.createNote(
                createOrEditNoteRequestBody.getContent(),
                createOrEditNoteRequestBody.getSlug(),
                createOrEditNoteRequestBody.getDeleteAfter(),
                createOrEditNoteRequestBody.getAccessToken()
        ));
    }

    @PostMapping("/edit")
    public Response editNote(@RequestBody CreateOrEditNoteRequestBody createOrEditNoteRequestBody) {
        return new CreatedOrEditedNoteResponse(mNoteService.editNote(
                createOrEditNoteRequestBody.getContent(),
                createOrEditNoteRequestBody.getSlug(),
                createOrEditNoteRequestBody.getAccessToken()
        ));
    }

    @GetMapping("/delete/{slug}")
    public Response deleteNote(@PathVariable String slug, @RequestParam(value = "accessToken") String accessToken) {
        mNoteService.deleteNote(slug, accessToken);
        return new DeletedNoteResponse();
    }

    @GetMapping("/getAll")
    public Response getNotes(@RequestParam(value = "accessToken") String accessToken) {
        return new UserNotesResponse(mNoteService.userNotes(accessToken));
    }

    @GetMapping("/get/{slug}")
    public Response getNote(@PathVariable String slug, @RequestParam(value = "accessToken", required = false) String accessToken) {
        return new UserNoteResponse(mNoteService.getNote(slug, accessToken));
    }
}
