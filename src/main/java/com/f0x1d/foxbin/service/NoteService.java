package com.f0x1d.foxbin.service;

import com.f0x1d.foxbin.database.model.FoxBinNote;
import com.f0x1d.foxbin.database.model.FoxBinUser;
import com.f0x1d.foxbin.repository.NoteRepository;
import com.f0x1d.foxbin.restcontroller.note.exceptions.EmptyContentException;
import com.f0x1d.foxbin.restcontroller.note.exceptions.NoSuchNoteException;
import com.f0x1d.foxbin.restcontroller.note.exceptions.SlugTakenException;
import com.f0x1d.foxbin.utils.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository mNoteRepository;

    @Autowired
    private RandomStringGenerator mRandomStringGenerator;

    public List<FoxBinNote> userNotes(String accessToken) {
        return mNoteRepository.userNotes(accessToken);
    }

    public String getRawNote(String slug) {
        FoxBinNote foxBinNote = mNoteRepository.noteFromSlug(slug);
        if (foxBinNote == null)
            throw new NoSuchNoteException();

        return foxBinNote.getContent();
    }

    public String createNote(String content, String slug, String accessToken) {
        if (content.isEmpty())
            throw new EmptyContentException();

        FoxBinUser user = null;
        if (accessToken != null)
            user = mNoteRepository.userFromAccessToken(accessToken);

        slug = generateSlug(slug);

        mNoteRepository.createNote(content, slug, user);

        return slug;
    }

    private String generateSlug(String slug) {
        if (slug != null) {
            if (mNoteRepository.noteFromSlug(slug) == null)
                return slug;
            else
                throw new SlugTakenException();
        }

        slug = mRandomStringGenerator.nextSlug();
        while (mNoteRepository.noteFromSlug(slug) != null)
            slug = mRandomStringGenerator.nextSlug();

        return slug;
    }
}
