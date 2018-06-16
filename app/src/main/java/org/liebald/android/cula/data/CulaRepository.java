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
import android.util.Log;

import org.liebald.android.cula.data.database.LibraryDao;
import org.liebald.android.cula.data.database.LibraryEntry;
import org.liebald.android.cula.utilities.AppExecutors;

import java.util.List;

/**
 * Handles data operations in Cula.
 */
public class CulaRepository {

    private static final String LOG_TAG = CulaRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static CulaRepository sInstance;
    private final LibraryDao mLibraryDao;
    private final AppExecutors mExecutors;

    private CulaRepository(LibraryDao libraryDao, AppExecutors appExecutors) {
        mLibraryDao = libraryDao;
        mExecutors = appExecutors;

        //TODO: remove following testcode:
        mExecutors.diskIO().execute(mLibraryDao::deleteAll);
        LibraryEntry entry1 = new LibraryEntry(1, "native1", "foreign", 1.1);
        addLibraryEntry(entry1);
        LibraryEntry entry2 = new LibraryEntry(2, "native2", "foreign2", 2.2);
        addLibraryEntry(entry2);
        LibraryEntry entry3 = new LibraryEntry(3, "native3", "foreign3", 3.3);
        addLibraryEntry(entry3);
        LibraryEntry entry4 = new LibraryEntry(4, "native4", "foreign44", 4.4);
        addLibraryEntry(entry4);
        LibraryEntry entry5 = new LibraryEntry(5, "native5", "foreign43", 4.8);
        addLibraryEntry(entry5);
        mExecutors.diskIO().execute(() -> Log.d(CulaRepository.class.getSimpleName(), "Database has now " + mLibraryDao.getLibrarySize() + " entries"));

    }

    public synchronized static CulaRepository getInstance(
            LibraryDao libraryDao, AppExecutors appExecutors) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new CulaRepository(libraryDao, appExecutors);
                Log.d(LOG_TAG, "Made new repository");
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
        return mLibraryDao.getAllEntries();
    }

    /**
     * Get all {@link LibraryEntry}s.
     *
     * @return The {@link LibraryEntry} with the given ID.
     */
    public LiveData<LibraryEntry> getLibraryEntry(int id) {
        return mLibraryDao.getEntriyById(id);
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
}