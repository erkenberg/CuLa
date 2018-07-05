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

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import org.liebald.android.cula.data.database.CulaDatabase;
import org.liebald.android.cula.data.database.Dao.LanguageDao;
import org.liebald.android.cula.data.database.Dao.LessonDao;
import org.liebald.android.cula.data.database.Dao.LibraryDao;
import org.liebald.android.cula.data.database.Dao.QuoteDao;
import org.liebald.android.cula.data.database.Entities.LanguageEntry;
import org.liebald.android.cula.data.database.Entities.LessonEntry;
import org.liebald.android.cula.data.database.Entities.LessonMappingEntry;
import org.liebald.android.cula.data.database.Entities.LibraryEntry;
import org.liebald.android.cula.data.database.Entities.MappingPOJO;
import org.liebald.android.cula.data.database.Entities.QuoteEntry;
import org.liebald.android.cula.services.UpdateQuoteJobService;
import org.liebald.android.cula.utilities.AppExecutors;

import java.util.List;

/**
 * Handles data operations in Cula.
 */
public class CulaRepository {

    /**
     * Tag for logging.
     */
    private static final String TAG = CulaRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    @SuppressLint("StaticFieldLeak")
    private static CulaRepository sInstance;
    private final LibraryDao mLibraryDao;
    private final LanguageDao mLanguageDao;
    private final QuoteDao mQuoteDao;
    private final LessonDao mLessonDao;
    private final AppExecutors mExecutors;
    private final SharedPreferences mSharedPreferences;
    private final Context mContext;


    private CulaRepository(CulaDatabase database, AppExecutors appExecutors, SharedPreferences sharedPreferences, Context context) {
        mLibraryDao = database.libraryDao();
        mExecutors = appExecutors;
        mLanguageDao = database.languageDao();
        mQuoteDao = database.quoteDao();
        mLessonDao = database.lessonDao();
        mSharedPreferences = sharedPreferences;
        mContext = context;

        //TODO: remove following testcode before publishing
        setDebugState();


        scheduleJobService();
    }

    /**
     * Singleton to make sure only one {@link CulaRepository} is used at a time.
     *
     * @param database          The {@link CulaDatabase} to access all {@link android.arch.persistence.room.Dao}s.
     * @param appExecutors      The {@link AppExecutors} used to execute all kind of queries of the main thread.
     * @param sharedPreferences The {@link SharedPreferences} used access the apps settings.
     * @return A new {@link CulaRepository} if none exists. If already an instance exists this is returned instead of creating a new one.
     */
    public synchronized static CulaRepository getInstance(
            CulaDatabase database, AppExecutors appExecutors, SharedPreferences sharedPreferences, Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new CulaRepository(database, appExecutors, sharedPreferences, context);
                Log.d(TAG, "Made new repository");
            }
        }
        return sInstance;
    }


    /**
     * Schedules a Job service to regularly update the motivational quote.
     */
    private void scheduleJobService() {

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(mContext));

        // Update the database immediately
        Job myInstantJob = dispatcher.newJobBuilder()
                .setService(UpdateQuoteJobService.class)
                .setTag("updateQuoteJobServiceNow")
                .build();
        dispatcher.mustSchedule(myInstantJob);

        // and also update it each 12-13 hours as recurring job
        Job recurringJob = dispatcher.newJobBuilder()
                .setService(UpdateQuoteJobService.class)
                .setTag("updateQuoteJobServiceRecurrent")
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        12 * 60 * 60,
                        13 * 60 * 60))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .build();
        dispatcher.mustSchedule(recurringJob);
    }

    /**
     * For debugging purposes prefill database with specified data.
     */
    private void setDebugState() {
        mExecutors.diskIO().execute(mLibraryDao::deleteAll);
        insertLanguageEntry(new LanguageEntry("German"));
        insertLanguageEntry(new LanguageEntry("Greek"));
        insertLibraryEntry(new LibraryEntry(1, "native1", "foreign", "German", 1.1));
        insertLibraryEntry(new LibraryEntry(2, "native2", "foreign2", "German", 2.2));
        insertLibraryEntry(new LibraryEntry(3, "native3", "foreign3", "German", 3.3));
        insertLibraryEntry(new LibraryEntry(4, "native4", "foreign4", "German", 4.4));
        insertLibraryEntry(new LibraryEntry(5, "native5", "foreign5", "German", 4.8));
        insertLibraryEntry(new LibraryEntry(6, "native6", "foreign6", "Greek", 2));
        insertLibraryEntry(new LibraryEntry(7, "native7", "foreign7", "Greek", 4));
        insertQuoteEntry(new QuoteEntry(1, "TestQuote of the Day with a rather medium long text", "Stefan"));
        insertLessonEntry(new LessonEntry(1, "Test Lesson 1", "This lesson is for testing purposes", "German"));
        insertLessonEntry(new LessonEntry(2, "Test Lesson 2", "This lesson is for testing purposes", "Greek"));
        insertLessonEntry(new LessonEntry(3, "Test Lesson 3", "This lesson is for testing purposes", "Greek"));
        insertLessonMappingEntry(new LessonMappingEntry(1, 1, 1));
        insertLessonMappingEntry(new LessonMappingEntry(2, 1, 2));
        insertLessonMappingEntry(new LessonMappingEntry(3, 2, 3));
        insertLessonMappingEntry(new LessonMappingEntry(4, 3, 4));
        insertLessonMappingEntry(new LessonMappingEntry(5, 3, 5));

        //print the current entries in the db to the log console.
        mExecutors.diskIO().execute(() ->
                Log.d(CulaRepository.class.getSimpleName(), "Database has now " + mLibraryDao.getLibrarySize() + " library entries")
        );
        mExecutors.diskIO().execute(() ->
                Log.d(CulaRepository.class.getSimpleName(), "Database has now " + mLanguageDao.getAmountOfLanguages() + " language entries")
        );
        mExecutors.diskIO().execute(() ->
                Log.d(CulaRepository.class.getSimpleName(), "Database has now " + mLessonDao.getAmountOfLessons() + " lesson entries")
        );
        mExecutors.diskIO().execute(() ->
                Log.d(CulaRepository.class.getSimpleName(), "Database has now " + mLessonDao.getAmountOfLessonsMappings() + " lesson mapping entries")
        );

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
    public void insertLibraryEntry(LibraryEntry... libraryEntry) {
        mExecutors.diskIO().execute(() -> mLibraryDao.insertEntry(libraryEntry));
        for (LibraryEntry lib :
                libraryEntry) {
            Log.d(TAG, "Added Entry to db:" + lib.toString());
        }
    }

    /**
     * Removes the given {@link LibraryEntry}s from the Database.
     *
     * @param libraryEntry The @{@link LibraryEntry}s to remove from the Database
     */
    public void deleteLibraryEntry(LibraryEntry libraryEntry) {
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
     * Removes the given {@link LanguageEntry}s from the Database.
     *
     * @param languageEntry The @{@link LanguageEntry}s to remove from the Database
     */
    public void deleteLanguageEntry(LanguageEntry languageEntry) {
        mExecutors.diskIO().execute(() -> mLanguageDao.deleteEntry(languageEntry));
    }

    /**
     * Adds the given {@link LanguageEntry}s to the Database. If the language already exists, nothing is done.
     *
     * @param languageEntries One or more {@link LanguageEntry}s to add to the Database
     */
    public void insertLanguageEntry(LanguageEntry... languageEntries) {
        mExecutors.diskIO().execute(() -> mLanguageDao.insertEntry(languageEntries));
    }


    /**
     * Adds the given {@link QuoteEntry}s to the Database.
     *
     * @param quoteEntries One or more {@link QuoteEntry}s to add to the Database
     */
    public void insertQuoteEntry(QuoteEntry... quoteEntries) {
        mExecutors.diskIO().execute(() -> mQuoteDao.insertEntry(quoteEntries));
    }

    /**
     * Load a new {@link QuoteEntry} and return it wrapped in {@link LiveData}.
     *
     * @return The {@link LiveData} wrapped {@link QuoteEntry}
     */
    public LiveData<QuoteEntry> getQuote() {
        return mQuoteDao.getLatestEntry();
    }


    /**
     * Get all {@link LessonEntry}s.
     *
     * @return All {@link LessonEntry}s.
     */
    public LiveData<List<LessonEntry>> getAllLessonEntries() {
        //todo: find out how to use the string resource here instead of the hard coded key.
        String language = mSharedPreferences.getString("languages", "123");
        Log.d(TAG, "Retrieving all entries for selected Language: " + language);
        return mLessonDao.getAllEntries(language);
    }

    /**
     * Adds the given {@link LessonEntry}s to the Database. If the {@link LessonEntry} already exists, nothing is done.
     *
     * @param lessonEntries One or more {@link LessonEntry}s to add to the Database
     */
    public void insertLessonEntry(LessonEntry... lessonEntries) {
        mExecutors.diskIO().execute(() -> mLessonDao.insertEntry(lessonEntries));
    }

    /**
     * Removes the given {@link LessonEntry}s from the Database.
     *
     * @param lessonEntry The @{@link LessonEntry}s to remove from the Database
     */
    public void deleteLessonEntry(LessonEntry lessonEntry) {
        mExecutors.diskIO().execute(() -> mLessonDao.deleteEntry(lessonEntry));
    }

    /**
     * Get the {@link LessonEntry} with the given ID.
     *
     * @return The {@link LessonEntry} with the given ID.
     */
    public LiveData<LessonEntry> getLessonEntry(int id) {
        return mLessonDao.getEntryById(id);
    }

    /**
     * Adds the given {@link LessonMappingEntry}s to the Database. If the {@link LessonMappingEntry} already exists, nothing is done.
     *
     * @param lessonMappingEntries One or more {@link LanguageEntry}s to add to the Database
     */
    public void insertLessonMappingEntry(LessonMappingEntry... lessonMappingEntries) {
        mExecutors.diskIO().execute(() -> mLessonDao.insertMappingEntry(lessonMappingEntries));
    }

    /**
     * Deletes a mapping between a {@link LessonEntry} and a {@link LibraryEntry}.
     *
     * @param lessonMappingEntry The {@link LessonMappingEntry} describing the mapping.
     */
    public void deleteLessonMappingEntry(LessonMappingEntry lessonMappingEntry) {
        mExecutors.diskIO().execute(() -> mLessonDao.deleteMappingEntry(
                lessonMappingEntry.getLessonEntryId(), lessonMappingEntry.getLibraryEntryId()));

    }

    /**
     * Gets the List of {@link MappingPOJO}s in the lesson database table with the given id
     * based on the current language.
     *
     * @param id The lesson id for which the {@link List} of {@link MappingPOJO}s
     *           should be retrieved.
     * @return {@link LiveData} with the {@link List} of @{@link MappingPOJO}s.
     */
    public LiveData<List<MappingPOJO>> getMappingEntries(int id) {
        //todo: find out how to use the string resource here instead of the hard coded key.
        String language = mSharedPreferences.getString("languages", "123");
        return mLessonDao.getLessonMappingById(id, language);
    }


}