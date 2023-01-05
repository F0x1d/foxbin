package com.f0x1d.foxbin.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CryptUtils {

    // xd
    public static String toMd5(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            return Base64
                    .getEncoder()
                    .encodeToString(
                            messageDigest.digest(
                                    password.getBytes(StandardCharsets.UTF_8)
                            )
                    );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
