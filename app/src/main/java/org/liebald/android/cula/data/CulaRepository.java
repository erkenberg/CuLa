/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.liebald.android.cula.data;

import android.arch.lifecycle.LiveData;
import android.content.SharedPreferences;
import android.util.Log;

import org.liebald.android.cula.data.database.LanguageDao;
import org.liebald.android.cula.data.database.LanguageEntry;
import org.liebald.android.cula.data.database.LibraryDao;
import org.liebald.android.cula.data.database.LibraryEntry;
import org.liebald.android.cula.utilities.AppExecutors;

import java.util.List;

/**
 * Handles data operations in Cula.
 */
public class CulaRepository {

    private static final String TAG = CulaRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static CulaRepository sInstance;
    private final LibraryDao mLibraryDao;
    private final LanguageDao mLanguageDao;
    private final AppExecutors mExecutors;
    private final SharedPreferences mSharedPreferences;

    private CulaRepository(LibraryDao libraryDao, LanguageDao languageDao, AppExecutors appExecutors, SharedPreferences sharedPreferences) {
        mLibraryDao = libraryDao;
        mExecutors = appExecutors;
        mLanguageDao = languageDao;
        mSharedPreferences = sharedPreferences;

        //TODO: remove following testcode:
        mExecutors.diskIO().execute(mLibraryDao::deleteAll);
        LanguageEntry language1 = new LanguageEntry("German");
        addLanguageEntry(language1);
        LanguageEntry language2 = new LanguageEntry("Greek");
        addLanguageEntry(language2);
        LibraryEntry entry1 = new LibraryEntry(1, "native1", "foreign", "German", 1.1);
        addLibraryEntry(entry1);
        LibraryEntry entry2 = new LibraryEntry(2, "native2", "foreign2", "German", 2.2);
        addLibraryEntry(entry2);
        LibraryEntry entry3 = new LibraryEntry(3, "native3", "foreign3", "German", 3.3);
        addLibraryEntry(entry3);
        LibraryEntry entry4 = new LibraryEntry(4, "native4", "foreign4", "German", 4.4);
        addLibraryEntry(entry4);
        LibraryEntry entry5 = new LibraryEntry(5, "native5", "foreign5", "German", 4.8);
        addLibraryEntry(entry5);
        LibraryEntry entry6 = new LibraryEntry(6, "native6", "foreign6", "Greek", 2);
        addLibraryEntry(entry6);
        LibraryEntry entry7 = new LibraryEntry(7, "native7", "foreign7", "Greek", 4);
        addLibraryEntry(entry7);
        mExecutors.diskIO().execute(() -> Log.d(CulaRepository.class.getSimpleName(), "Database has now " + mLibraryDao.getLibrarySize() + " library entries"));

    }

    /**
     * Singleton to make sure only one {@link CulaRepository} is used at a time.
     *
     * @param libraryDao   The {@link LibraryDao} to access all {@link LibraryEntry}s.
     * @param languageDao  The {@link LanguageDao} to access all {@link LanguageEntry}s.
     * @param appExecutors The {@link AppExecutors} used to execute all kind of queries of the main thread.
     * @param sharedPreferences The {@link SharedPreferences} used access the apps settings.
     * @return A new {@link CulaRepository} if none exists. If already an instance exists this is returned instead of creating a new one.
     */
    public synchronized static CulaRepository getInstance(
            LibraryDao libraryDao, LanguageDao languageDao, AppExecutors appExecutors, SharedPreferences sharedPreferences) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new CulaRepository(libraryDao, languageDao, appExecutors, sharedPreferences);
                Log.d(TAG, "Made new repository");
            }
        }
        return sInstance;
    }


    /**
     * Get all {@link LibraryEntry}s.
     *
     * @return All {@link LibraryEntry}s.
     */
    public LiveData<List<LibraryEntry>> getAllLibraryEntries() {
        //todo: find out how to use the string resource here instead of the hard coded key.
        String language = mSharedPreferences.getString("languages", "123");
        Log.d(TAG, "Retrieving all entries for selected Language: " + language);
        return mLibraryDao.getAllEntries(language);
    }

    /**
     * Get all {@link LibraryEntry}s.
     *
     * @return The {@link LibraryEntry} with the given ID.
     */
    public LiveData<LibraryEntry> getLibraryEntry(int id) {
        return mLibraryDao.getEntryById(id);
    }

    /**
     * Adds the given {@link LibraryEntry}s to the Database.
     *
     * @param libraryEntry One or more {@link LibraryEntry}s to add to the Database
     */
    public void addLibraryEntry(LibraryEntry... libraryEntry) {
        mExecutors.diskIO().execute(() -> mLibraryDao.insertEntry(libraryEntry));
    }

    /**
     * Removes the given {@link LibraryEntry}s from the Database.
     *
     * @param libraryEntry The @{@link LibraryEntry}s to remove from the Database
     */
    public void removeLibraryEntry(LibraryEntry libraryEntry) {
        mExecutors.diskIO().execute(() -> mLibraryDao.deleteEntry(libraryEntry));
    }

    /**
     * Get all {@link LanguageEntry}s.
     *
     * @return All {@link LanguageEntry}s.
     */
    public LiveData<List<LanguageEntry>> getAllLanguageEntries() {
        return mLanguageDao.getAllEntries();
    }

    /**
     * Adds the given {@link LanguageEntry}s to the Database.
     *
     * @param languageEntries One or more {@link LanguageEntry}s to add to the Database
     */
    public void addLanguageEntry(LanguageEntry... languageEntries) {
        mExecutors.diskIO().execute(() -> mLanguageDao.insertEntry(languageEntries));
    }


}