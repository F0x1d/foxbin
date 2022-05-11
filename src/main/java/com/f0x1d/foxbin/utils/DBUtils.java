package com.f0x1d.foxbin.utils;

import com.f0x1d.foxbin.database.ObjectBox;
import com.f0x1d.foxbin.database.model.AccessToken;
import com.f0x1d.foxbin.database.model.FoxBinNote;
import com.f0x1d.foxbin.database.model.FoxBinUser;
import io.objectbox.Box;
import io.objectbox.query.Query;

import java.util.List;

public class DBUtils {

    public static Box<FoxBinUser> boxForUsers() {
        return ObjectBox.get().boxFor(FoxBinUser.class);
    }

    public static Box<FoxBinNote> boxForNotes() {
        return ObjectBox.get().boxFor(FoxBinNote.class);
    }

    public static Box<AccessToken> boxForTokens() {
        return ObjectBox.get().boxFor(AccessToken.class);
    }

    public static <T> List<T> find(Query<T> query) {
        List<T> t = query.find();
        query.close();
        return t;
    }

    public static <T> T findFirst(Query<T> query) {
        T t = query.findFirst();
        query.close();
        return t;
    }

    public static void closeThreadResources() {
        ObjectBox.get().closeThreadResources();
    }
}
