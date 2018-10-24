package com.sliebald.cula.data.database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sliebald.cula.data.database.CulaDatabase;
import com.sliebald.cula.data.database.Entities.QuoteEntry;

/**
 * {@link Dao} which provides an api for all data operations with the {@link CulaDatabase}
 * related to the {@link .QuoteEntry}s.
 */
@Dao
public interface QuoteDao {

    /**
     * 6
     * Inserts a {@link QuoteEntry} into the quotes table. If there is a conflicting id the
     * {@link QuoteEntry} uses the {@link OnConflictStrategy} to replace the {@link QuoteEntry}.
     * The required uniqueness of these values is defined in the {@link QuoteEntry}.
     *
     * @param quoteEntries A list of {@link QuoteEntry}s to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEntry(QuoteEntry... quoteEntries);

    /**
     * Gets the latest {@link QuoteEntry}s in the quotes database table.
     *
     * @return {@link LiveData} with the latest @{@link QuoteEntry}.
     */
    @Query("SELECT id, text, author, createdAt FROM quotes ORDER by id desc LIMIT 1")
    LiveData<QuoteEntry> getLatestEntry();


}