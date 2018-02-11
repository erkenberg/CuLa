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

package com.liebald.android.cula.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.liebald.android.cula.data.database.DictionaryDao;
import com.liebald.android.cula.data.database.DictionaryEntry;
import com.liebald.android.cula.utilities.AppExecutors;

import java.util.List;

/**
 * Handles data operations in Cula.
 */
public class CulaRepository {

    private static final String LOG_TAG = CulaRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static CulaRepository sInstance;
    private final DictionaryDao mDictionaryDao;
    private final AppExecutors mExecutors;

    private CulaRepository(DictionaryDao dictionaryDao, AppExecutors appExecutors) {
        mDictionaryDao = dictionaryDao;
        mExecutors = appExecutors;

        mExecutors.diskIO().execute(() -> {
            mDictionaryDao.deleteAll();
        });
        DictionaryEntry entry1 = new DictionaryEntry(1, "native", "foreign");
        addDictionaryEntry(entry1);
        DictionaryEntry entry2 = new DictionaryEntry(2, "native2", "foreign2");
        addDictionaryEntry(entry2);
        DictionaryEntry entry3 = new DictionaryEntry(3, "native3", "foreign3");
        addDictionaryEntry(entry3);
        DictionaryEntry entry4 = new DictionaryEntry(4, "native44", "foreign44");
        addDictionaryEntry(entry4);

        //int c = mDictionaryDao.insertEntry(entry1, entry2, entry3, entry4).size();
        // Log.d(CulaRepository.class.getSimpleName(), "initialized database with " + c + " entries");
        mExecutors.diskIO().execute(() -> {
            Log.d(CulaRepository.class.getSimpleName(), "Database has now " + mDictionaryDao.getDictionarySize() + " entries");
        });

    }

    public synchronized static CulaRepository getInstance(
            DictionaryDao dictionaryDao, AppExecutors appExecutors) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new CulaRepository(dictionaryDao, appExecutors);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }


    /**
     * Get all {@link com.liebald.android.cula.data.database.DictionaryEntry}s.
     *
     * @return The Weather from the chosen date.
     */
    public LiveData<List<DictionaryEntry>> getAllDictionaryEntries() {
        return mDictionaryDao.getAllEntries();
    }

    /**
     * Adds the given DictionaryEntries to the Database.
     *
     * @param dictionaryEntry One or more @{@link DictionaryEntry}s to add to the Database
     */
    public void addDictionaryEntry(DictionaryEntry... dictionaryEntry) {
        mExecutors.diskIO().execute(() -> {
            mDictionaryDao.insertEntry(dictionaryEntry);
        });
    }


}