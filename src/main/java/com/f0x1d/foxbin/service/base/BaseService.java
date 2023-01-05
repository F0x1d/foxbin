package com.f0x1d.foxbin.service.base;

import com.f0x1d.foxbin.repository.NoteRepository;
import com.f0x1d.foxbin.repository.UserRepository;
import com.f0x1d.foxbin.utils.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class BaseService {

    @Autowired
    protected NoteRepository mNoteRepository;
    @Autowired
    protected UserRepository mUserRepository;

    @Autowired
    protected RandomStringGenerator mRandomStringGenerator;

    protected boolean anyEmpty(String... params) {
        return Arrays.stream(params).anyMatch(str -> str == null || str.isEmpty());
    }
}
