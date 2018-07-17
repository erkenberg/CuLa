package org.liebald.android.cula.data.database.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import org.liebald.android.cula.data.database.CulaDatabase;
import org.liebald.android.cula.data.database.Entities.StatisticEntry;
import org.liebald.android.cula.data.database.Pojos.StatisticsLibraryWordCount;

import java.util.List;

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

    @Query("select " +
            "  case " +
            "    when knowledgeLevel between 0 and 0.999999 then '0'" +
            "    when knowledgeLevel between 1 and 1.999999 then '1'" +
            "    when knowledgeLevel between 2 and 2.999999 then '2'" +
            "    when knowledgeLevel between 3 and 3.999999 then '3'" +
            "    when knowledgeLevel between 4 and 5 then '4'" +
            "  end as `level`," +
            "  count(1) as `count` " +
            "FROM library " +
            "WHERE language = (SELECT language FROM language WHERE isActive=1 LIMIT 1) " +
            "GROUP BY `level`")
    LiveData<List<StatisticsLibraryWordCount>> getStatisticsLibraryCountByKnowledgeLevel();
}