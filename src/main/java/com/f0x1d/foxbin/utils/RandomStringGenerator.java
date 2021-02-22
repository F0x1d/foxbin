package com.f0x1d.foxbin.utils;

import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;

public class RandomStringGenerator {

    private static RandomStringGenerator sInstance;

    public static RandomStringGenerator getInstance() {
        return sInstance == null ? sInstance = new RandomStringGenerator() : sInstance;
    }

    private String nextRandom(char[] buf) {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = mSymbols[mRandom.nextInt(mSymbols.length)];
        return new String(buf);
    }

    public String nextToken() {
        return nextRandom(mTokenBuf);
    }

    public String nextSlug() {
        return nextRandom(mSlugBuf);
    }

    public static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER = UPPER.toLowerCase(Locale.ROOT);
    public static final String DIGITS = "0123456789";
    public static final String ALPHANUM = UPPER + LOWER + DIGITS;

    private final Random mRandom;
    private final char[] mSymbols;
    private final char[] mTokenBuf;
    private final char[] mSlugBuf;

    public RandomStringGenerator() {
        if (ALPHANUM.length() < 2) throw new IllegalArgumentException();
        this.mRandom = new SecureRandom();
        this.mSymbols = ALPHANUM.toCharArray();
        this.mTokenBuf = new char[100];
        this.mSlugBuf = new char[10];
    }
}
