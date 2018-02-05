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

package android.liebald.com.cula.data;

import android.arch.lifecycle.LiveData;
import android.liebald.com.cula.data.database.DictionaryDao;
import android.liebald.com.cula.data.database.DictionaryEntry;
import android.util.Log;

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
    private boolean mInitialized = false;

    private CulaRepository(DictionaryDao dictionaryDao) {
        mDictionaryDao = dictionaryDao;
    }

    public synchronized static CulaRepository getInstance(
            DictionaryDao dictionaryDao) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new CulaRepository(dictionaryDao);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    /**
     * Creates periodic sync tasks and checks to see if an immediate sync is required. If an
     * immediate sync is required, this method will take care of making sure that sync occurs.
     */
    private synchronized void initializeData() {

        // Only perform initialization once per app lifetime. If initialization has already been
        // performed, we have nothing to do in this method.
        if (mInitialized) {
            return;
        }
        DictionaryEntry entry1 = new DictionaryEntry(1, "native", "foreign");
        DictionaryEntry entry2 = new DictionaryEntry(2, "native2", "foreign2");
        DictionaryEntry entry3 = new DictionaryEntry(3, "native3", "foreign3");
        DictionaryEntry entry4 = new DictionaryEntry(4, "native4", "foreign4");
        int c = mDictionaryDao.insertEntry(entry1, entry2, entry3, entry4).size();
        Log.d(CulaRepository.class.getSimpleName(), "initialized database with " + c + " entries");
        Log.d(CulaRepository.class.getSimpleName(), "Database has now " + mDictionaryDao.getDictionarySize() + " entries");

        mInitialized = true;

    }

    /**
     * Get all {@link android.liebald.com.cula.data.database.DictionaryEntry}s.
     *
     * @return The Weather from the chosen date.
     */
    public LiveData<List<DictionaryEntry>> getAllDictionaryEntries() {
        initializeData();
        return mDictionaryDao.getAllEntries();// mDictionaryDao.getAllEntries();
    }


}