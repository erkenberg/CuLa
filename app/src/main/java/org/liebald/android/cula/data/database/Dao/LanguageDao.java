package org.liebald.android.cula.data.database.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import org.liebald.android.cula.data.database.CulaDatabase;
import org.liebald.android.cula.data.database.Entities.LanguageEntry;

import java.util.List;

/**
 * {@link Dao} which provides an api for all data operations with the {@link CulaDatabase} related to the languages.
 */
@Dao
public interface LanguageDao {

    /**
     * Inserts a {@link LanguageEntry} into the language table. If there is a conflicting id the
     * {@link LanguageEntry} uses the {@link OnConflictStrategy} to abort if the the {@link LanguageEntry} already exists.
     * The required uniqueness of these values is defined in the {@link LanguageEntry}.
     *
     * @param languageEntries A list of {@link LanguageEntry}s to insert
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertEntry(LanguageEntry... languageEntries);

    /**
     * Gets all {@link LanguageEntry}s in the language database table.
     *
     * @return {@link LiveData} with all @{@link LanguageEntry}s.
     */
    @Query("SELECT language FROM language ORDER by language desc")
    LiveData<List<LanguageEntry>> getAllEntries();


    /**
     * Get the current amount of entries in the language table.
     *
     * @return The current size.
     */
    @Query("Select count(language) from language")
    int getAmountOfLanguages();

    /**
     * Delete the given {@link LanguageEntry} from the language table.
     */
    @Delete
    void deleteEntry(LanguageEntry entry);
}