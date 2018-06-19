package org.liebald.android.cula.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * {@link Dao} which provides an api for all data operations with the {@link CulaDatabase} related to the library.
 */
@Dao
public interface LibraryDao {

    /**
     * Inserts a {@link LibraryEntry} into the library table. If there is a conflicting id the
     * libraryEntry uses the {@link OnConflictStrategy} of replacing the word pair.
     * The required uniqueness of these values is defined in the {@link LibraryEntry}.
     *
     * @param libraryEntry A list of {@link LibraryEntry}s to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEntry(LibraryEntry... libraryEntry);

    /**
     * Gets all {@link LibraryEntry}s in the library database table matching the given language.
     *
     * @param language The Language that must be included in all returned libraryEntries.
     * @return {@link LiveData} with all matching @{@link LibraryEntry}s.
     */
    @Query("SELECT id, nativeWord, foreignWord, language, knowledgeLevel FROM library WHERE language=:language ORDER by nativeWord desc")
    LiveData<List<LibraryEntry>> getAllEntries(String language);

    /**
     * Gets the {@link LibraryEntry} in the library database table with the given id.
     *
     * @param id The id of the entry.
     * @return {@link LiveData} with the @{@link LibraryEntry}.
     */
    @Query("SELECT id, nativeWord, foreignWord, language, knowledgeLevel FROM library WHERE id=:id")
    LiveData<LibraryEntry> getEntryById(int id);

    /**
     * Get the current amount of entries in the library table.
     *
     * @return The current size.
     */
    @Query("Select count(id) from library")
    int getLibrarySize();

    /**
     * Delete all entries from the library table.
     */
    @Query("DELETE from library")
    void deleteAll();

    /**
     * Delete the given entrie from the library table.
     */
    @Delete
    void deleteEntry(LibraryEntry entry);
}