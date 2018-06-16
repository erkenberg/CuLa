package org.liebald.android.cula.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * The Room Database for the App.
 */
@Database(version = 1, entities = {LibraryEntry.class, LanguageEntry.class}, exportSchema = false)
public abstract class CulaDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "CulaDatabase";
    private static final Object LOCK = new Object();
    private static volatile CulaDatabase sInstance;

    public static CulaDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(), CulaDatabase.class,
                            CulaDatabase.DATABASE_NAME).build();
                }
            }
        }
        return sInstance;
    }

    public abstract LibraryDao libraryDao();

    public abstract LanguageDao languageDao();

}