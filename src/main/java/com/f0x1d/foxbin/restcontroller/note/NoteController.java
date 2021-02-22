package com.f0x1d.foxbin.restcontroller.note;

import com.f0x1d.foxbin.model.requestbody.CreateNoteRequestBody;
import com.f0x1d.foxbin.model.response.base.Response;
import com.f0x1d.foxbin.model.response.note.CreatedNoteResponse;
import com.f0x1d.foxbin.model.response.note.UserNoteResponse;
import com.f0x1d.foxbin.model.response.note.UserNotesResponse;
import com.f0x1d.foxbin.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class NoteController {

    @Autowired
    private NoteService mNoteService;

    @PostMapping("/notes/create")
    public Response createNote(@RequestBody CreateNoteRequestBody createNoteRequestBody) {
        return new CreatedNoteResponse(mNoteService.createNote(
                createNoteRequestBody.getContent(),
                createNoteRequestBody.getSlug(),
                createNoteRequestBody.getAccessToken()
        ));
    }

    @GetMapping("/notes/raw/{slug}")
    public String rawNote(@PathVariable String slug) {
        return mNoteService.getRawNote(slug);
    }

    @GetMapping("/notes/getAll")
    public Response getNotes(@RequestParam(value = "accessToken") String accessToken) {
        return new UserNotesResponse(mNoteService.userNotes(accessToken));
    }

    @GetMapping("/notes/get/{slug}")
    public Response getNote(@PathVariable String slug, @RequestParam(value = "accessToken", required = false) String accessToken) {
        return new UserNoteResponse(mNoteService.getNote(slug, accessToken));
    }
}
