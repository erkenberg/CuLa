package com.sliebald.cula.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sliebald.cula.data.database.entities.StatisticEntry
import com.sliebald.cula.data.database.pojos.StatisticsActivityEntry
import com.sliebald.cula.data.database.pojos.StatisticsLibraryWordCount
import com.sliebald.cula.data.database.CulaDatabase
import java.util.*

/**
 * [Dao] which provides an api for all data operations with the [CulaDatabase]
 * related to the [StatisticEntry]s.
 */
@Dao
interface StatisticsDao {
    /**
     * Inserts a [StatisticEntry] into the statistics table.
     *
     * @param statisticEntries A list of [StatisticEntry]s to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEntry(vararg statisticEntries: StatisticEntry)

    /**
     * Groups all entries in the library table of the database by knowledgeLevel and counts how
     * many entries are in each category (0-0.99999, 1-1.999999,...,4-5)
     *
     * @return The result as [List] of [StatisticsLibraryWordCount], [List]
     * size depending on the amount of KnowledgeLevel ranges with words in it.
     */
    //TODO: find a way to avoid showing 'level' as invalid, it is just the defined name (AS level)
    @Query("SELECT " +
            "  CASE " +
            "    WHEN knowledgeLevel BETWEEN 0 AND 0.999999 THEN '0'" +
            "    WHEN knowledgeLevel BETWEEN 1 AND 1.999999 THEN '1'" +
            "    WHEN knowledgeLevel BETWEEN 2 AND 2.999999 THEN '2'" +
            "    WHEN knowledgeLevel BETWEEN 3 AND 3.999999 THEN '3'" +
            "    WHEN knowledgeLevel BETWEEN 4 AND 5 THEN '4'" +
            "  END AS `level`," +
            "  count(1) AS `count` " +
            "FROM library " +
            "WHERE language = (SELECT language FROM language WHERE isActive=1 LIMIT 1) " +
            "GROUP BY `level`")
    fun getStatisticsLibraryCountByKnowledgeLevel(): LiveData<List<StatisticsLibraryWordCount>>

    /**
     * Returns the activity of the user after the given date. For each active day a
     * [StatisticsActivityEntry] is added to the list. Days without activity have no entry.
     *
     * @param date The date after which the activity should be queried.
     * @return The List of [StatisticsActivityEntry] for all active days.
     */
    //Based on https://stackoverflow.com/questions/40199091/group-by-day-when-column-is-in-unix timestamp
    @Query("SELECT strftime('%Y-%m-%d', trainingDate / 1000, 'unixepoch') as date, " +
            "COUNT(*) as activity " +
            "FROM statistics " +
            "WHERE trainingDate>:date " +
            "GROUP BY strftime('%Y-%m-%d', trainingDate / 1000, 'unixepoch')" +
            "ORDER BY strftime('%Y-%m-%d', trainingDate / 1000, 'unixepoch') ASC")
    fun getStatisticsActivity(date: Date): LiveData<List<StatisticsActivityEntry>>
}