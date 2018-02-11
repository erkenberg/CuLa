package com.liebald.android.cula.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * The Room Database for the Dictionary.
 */
@Database(version = 1, entities = {DictionaryEntry.class})
public abstract class CulaDatabase extends RoomDatabase {

    public abstract DictionaryDao dictionaryDao();

    private static final String DATABASE_NAME = "dictionary";
    private static final Object LOCK = new Object();
    private static volatile CulaDatabase sInstance;

    public static CulaDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(), CulaDatabase.class,
                            CulaDatabase.DATABASE_NAME).allowMainThreadQueries().build();
                }
            }
        }
        return sInstance;
    }

}