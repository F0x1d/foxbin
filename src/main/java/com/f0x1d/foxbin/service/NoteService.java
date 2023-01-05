package com.f0x1d.foxbin.service;

import com.f0x1d.foxbin.database.ObjectBox;
import com.f0x1d.foxbin.database.model.FoxBinNote;
import com.f0x1d.foxbin.database.model.FoxBinUser;
import com.f0x1d.foxbin.model.response.note.usernote.UserNoteWithContent;
import com.f0x1d.foxbin.restcontroller.note.exceptions.*;
import com.f0x1d.foxbin.restcontroller.user.exceptions.SomethingIsEmptyException;
import com.f0x1d.foxbin.service.base.BaseService;
import com.f0x1d.foxbin.utils.DBUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class NoteService extends BaseService {

    private final Pattern mSlugPattern = Pattern.compile("[a-zA-Z0-9]{5,64}");

    public List<FoxBinNote> userNotes(String accessToken) {
        List<FoxBinNote> notes = mUserRepository.userFromAccessToken(accessToken).getNotes();
        DBUtils.closeThreadResources();
        return notes;
    }

    public UserNoteWithContent getNote(String slug, String accessToken) {
        if (slug.isEmpty())
            throw new SomethingIsEmptyException();

        FoxBinNote foxBinNote = noteFromSlug(slug);
        FoxBinUser foxBinUser = userFromAccessToken(accessToken);

        boolean editable = foxBinUser != null && foxBinNote.getUser().getTarget().equals(foxBinUser);

        UserNoteWithContent userNoteWithContent = UserNoteWithContent.create(
                foxBinNote.getContent(),
                foxBinNote.getSlug(),
                foxBinNote.getDate(),
                editable
        );

        DBUtils.closeThreadResources();
        return userNoteWithContent;
    }

    public String createNote(String content, String slug, long deleteAfter, String accessToken) {
        if (anyEmpty(content))
            throw new EmptyContentException();

        FoxBinUser user = userFromAccessToken(accessToken);

        String resultSlug = ObjectBox.get().callInTxNoException(() -> {
            String finalSlug = generateSlug(slug);
            mNoteRepository.createNote(content, finalSlug, deleteAfter, user);

            return finalSlug;
        });

        DBUtils.closeThreadResources();
        return resultSlug;
    }

    public void deleteNote(String slug, String accessToken) {
        if (anyEmpty(slug, accessToken))
            throw new SomethingIsEmptyException();

        FoxBinNote foxBinNote = noteFromSlug(slug);
        FoxBinUser foxBinUser = userFromAccessToken(accessToken);

        if (foxBinUser == null || !foxBinNote.getUser().getTarget().equals(foxBinUser))
            throw new UneditableNoteException();

        mNoteRepository.deleteNote(foxBinNote);
        DBUtils.closeThreadResources();
    }

    public String editNote(String content, String slug, String accessToken) {
        if (anyEmpty(content, slug, accessToken))
            throw new SomethingIsEmptyException();

        FoxBinNote foxBinNote = noteFromSlug(slug);
        FoxBinUser foxBinUser = userFromAccessToken(accessToken);

        if (foxBinUser == null || !foxBinNote.getUser().getTarget().equals(foxBinUser))
            throw new UneditableNoteException();

        ObjectBox.get().runInTx(() -> mNoteRepository.editNote(content, foxBinNote));

        DBUtils.closeThreadResources();
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
