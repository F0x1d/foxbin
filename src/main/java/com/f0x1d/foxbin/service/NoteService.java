package com.f0x1d.foxbin.service;

import com.f0x1d.foxbin.database.model.FoxBinNote;
import com.f0x1d.foxbin.database.model.FoxBinUser;
import com.f0x1d.foxbin.model.response.note.usernote.UserNoteWithContent;
import com.f0x1d.foxbin.repository.NoteRepository;
import com.f0x1d.foxbin.repository.UserRepository;
import com.f0x1d.foxbin.restcontroller.note.exceptions.*;
import com.f0x1d.foxbin.utils.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class NoteService {

    @Autowired
    private NoteRepository mNoteRepository;
    @Autowired
    private UserRepository mUserRepository;

    @Autowired
    private RandomStringGenerator mRandomStringGenerator;

    private final Pattern mSlugPattern = Pattern.compile("[a-zA-Z0-9]{5,64}");

    public List<FoxBinNote> userNotes(String accessToken) {
        return mUserRepository.userFromAccessToken(accessToken).getNotes();
    }

    public String getRawNote(String slug) {
        return noteFromSlug(slug).getContent();
    }

    public UserNoteWithContent getNote(String slug, String accessToken) {
        FoxBinNote foxBinNote = noteFromSlug(slug);
        FoxBinUser foxBinUser = userFromAccessToken(accessToken);

        boolean editable = foxBinUser != null && foxBinNote.getUser().getTarget().equals(foxBinUser);

        return UserNoteWithContent.create(
                foxBinNote.getContent(),
                foxBinNote.getSlug(),
                foxBinNote.getDate(),
                editable
        );
    }

    public String createNote(String content, String slug, String accessToken) {
        if (content.isEmpty() || (slug != null && slug.isEmpty()))
            throw new EmptyContentException();

        FoxBinUser user = userFromAccessToken(accessToken);

        slug = generateSlug(slug);
        mNoteRepository.createNote(content, slug, user);

        return slug;
    }

    public String editNote(String content, String slug, String accessToken) {
        if (content.isEmpty())
            throw new EmptyContentException();

        FoxBinNote foxBinNote = noteFromSlug(slug);
        FoxBinUser foxBinUser = userFromAccessToken(accessToken);

        if (foxBinUser == null || !foxBinNote.getUser().getTarget().equals(foxBinUser))
            throw new UneditableNoteException();

        mNoteRepository.editNote(content, foxBinNote);

        return slug;
    }

    private FoxBinUser userFromAccessToken(String accessToken) {
        FoxBinUser user = null;
        if (accessToken != null)
            user = mUserRepository.userFromAccessToken(accessToken);

        return user;
    }

    private FoxBinNote noteFromSlug(String slug) {
        FoxBinNote foxBinNote = mNoteRepository.noteFromSlug(slug);
        if (foxBinNote == null)
            throw new NoSuchNoteException();

        return foxBinNote;
    }

    private String generateSlug(String slug) {
        if (slug != null) {
            if (mNoteRepository.noteFromSlug(slug) == null) {
                if (mSlugPattern.matcher(slug).matches())
                    return slug;

                throw new IncorrectSlugException();
            } else
                throw new SlugTakenException();
        }

        slug = mRandomStringGenerator.nextSlug();
        while (mNoteRepository.noteFromSlug(slug) != null)
            slug = mRandomStringGenerator.nextSlug();

        return slug;
    }
}
