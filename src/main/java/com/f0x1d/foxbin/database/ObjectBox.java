package com.f0x1d.foxbin.database;

import com.f0x1d.foxbin.database.model.MyObjectBox;
import io.objectbox.BoxStore;

public class ObjectBox {
    private static BoxStore boxStore;

    public static BoxStore get() {
        return boxStore == null ? boxStore = MyObjectBox.builder().build() : boxStore;
    }
}