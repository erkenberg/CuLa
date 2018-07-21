package com.sliebald.android.cula.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.sliebald.android.cula.data.database.Dao.LanguageDao;
import com.sliebald.android.cula.data.database.Dao.LessonDao;
import com.sliebald.android.cula.data.database.Dao.LibraryDao;
import com.sliebald.android.cula.data.database.Dao.QuoteDao;
import com.sliebald.android.cula.data.database.Dao.StatisticsDao;
import com.sliebald.android.cula.data.database.Entities.LanguageEntry;
import com.sliebald.android.cula.data.database.Entities.LessonEntry;
import com.sliebald.android.cula.data.database.Entities.LessonMappingEntry;
import com.sliebald.android.cula.data.database.Entities.LibraryEntry;
import com.sliebald.android.cula.data.database.Entities.QuoteEntry;
import com.sliebald.android.cula.data.database.Entities.StatisticEntry;

/**
 * The Room Database for the App.
 */
@Database(version = 1,
        entities = {
                LibraryEntry.class,
                LanguageEntry.class,
                QuoteEntry.class,
                LessonEntry.class,
                LessonMappingEntry.class,
                StatisticEntry.class},
        exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class CulaDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "CulaDatabase";
    private static final Object LOCK = new Object();
    private static volatile CulaDatabase sInstance;

    public static CulaDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            CulaDatabase.class,
                            CulaDatabase.DATABASE_NAME).build();
                }
            }
        }
        return sInstance;
    }

    /**
     * Gain access to the {@link LibraryDao} for interacting with the library related database
     * parts.
     *
     * @return The {@link LibraryDao}.
     */
    public abstract LibraryDao libraryDao();

    /**
     * Gain access to the {@link LanguageDao} for interacting with the language related database
     * parts.
     *
     * @return The {@link LanguageDao}.
     */
    public abstract LanguageDao languageDao();

    /**
     * Gain access to the {@link QuoteDao} for interacting with the quote related database parts.
     *
     * @return The {@link QuoteDao}.
     */
    public abstract QuoteDao quoteDao();

    /**
     * Gain access to the {@link LessonDao} for interacting with the lesson related database parts.
     *
     * @return The {@link QuoteDao}.
     */
    public abstract LessonDao lessonDao();

    /**
     * Gain access to the {@link StatisticsDao} for interacting with the statistics related
     * database parts.
     *
     * @return The {@link StatisticsDao}.
     */
    public abstract StatisticsDao statisticsDao();

}