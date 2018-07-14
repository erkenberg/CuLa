package org.liebald.android.cula.data.database.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import org.liebald.android.cula.data.database.CulaDatabase;
import org.liebald.android.cula.data.database.Entities.StatisticEntry;

/**
 * {@link Dao} which provides an api for all data operations with the {@link CulaDatabase}
 * related to the {@link StatisticEntry}s.
 */
@Dao
public interface StatisticsDao {

    /**
     * Inserts a {@link StatisticEntry} into the statistics table.
     *
     * @param statisticEntries A list of {@link StatisticEntry}s to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEntry(StatisticEntry... statisticEntries);


}