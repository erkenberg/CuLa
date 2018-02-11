package com.liebald.android.cula.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * {@link Dao} which provides an api for all data operations with the {@link CulaDatabase}
 */
@Dao
public interface DictionaryDao {

    /**
     * Inserts a {@link DictionaryEntry} into the dictionary table. If there is a conflicting id the
     * dictionary ntry uses the {@link OnConflictStrategy} of replacing the word pair.
     * The required uniqueness of these values is defined in the {@link DictionaryEntry}.
     *
     * @param dictionaryEntry A list of weather forecasts to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEntry(DictionaryEntry... dictionaryEntry);

    /**
     * Gets all {@link DictionaryEntry}s in the dictionary database table.
     *
     * @return {@link LiveData} with all DictionaryEntries.
     */
    @Query("SELECT id, nativeWord, foreignWord FROM dictionary ORDER by id desc")
    LiveData<List<DictionaryEntry>> getAllEntries();

    /**
     * Get the current amount of entries in the dictionary table.
     *
     * @return The current size.
     */
    @Query("Select count(id) from dictionary")
    int getDictionarySize();

    /**
     * Delete all entries from the dictionary table.
     */
    @Query("DELETE from dictionary")
    void deleteAll();
}