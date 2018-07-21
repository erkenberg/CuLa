package com.sliebald.cula.data.database.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.sliebald.cula.data.database.CulaDatabase;
import com.sliebald.cula.data.database.Entities.StatisticEntry;
import com.sliebald.cula.data.database.Pojos.LessonKnowledgeLevel;
import com.sliebald.cula.data.database.Pojos.StatisticsActivityEntry;
import com.sliebald.cula.data.database.Pojos.StatisticsLastTrainingDate;
import com.sliebald.cula.data.database.Pojos.StatisticsLibraryWordCount;

import java.util.Date;
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

    /**
     * Groups all entries in the library table of the database by knowledgeLevel and counts how
     * many entries are in each category (0-0.99999, 1-1.999999,...,4-5)
     *
     * @return The result as {@link List} of {@link StatisticsLibraryWordCount}, {@link List}
     * size depending on the amount of KnowledgeLevel ranges with words in it.
     */
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
    LiveData<List<StatisticsLibraryWordCount>> getStatisticsLibraryCountByKnowledgeLevel();


    /**
     * Returns the activity of the user after the given date. For each active day a
     * {@link StatisticsActivityEntry} is added to the list. Days without activity have no entry.
     *
     * @param date The date after which the activity should be queried.
     * @return The List of {@link StatisticsActivityEntry} for all active days.
     */
    //TODO: currently includes all languages! Filter for current language
    //Based on https://stackoverflow.com/questions/40199091/group-by-day-when-column-is-in
    // -unixtimestamp
    @Query("SELECT strftime('%Y-%m-%d', trainingDate / 1000, 'unixepoch') as date, " +
            "COUNT(*) as activity " +
            "FROM statistics " +
            "WHERE trainingDate>:date " +
            "GROUP BY strftime('%Y-%m-%d', trainingDate / 1000, 'unixepoch')" +
            "ORDER BY strftime('%Y-%m-%d', trainingDate / 1000, 'unixepoch') ASC")
    LiveData<List<StatisticsActivityEntry>> getStatisticsActivity(Date date);


    /**
     * Get the date of the last training.
     *
     * @return Date of the last training.
     */
    @Query("SELECT max(trainingDate) AS lastActive FROM statistics ")
    LiveData<StatisticsLastTrainingDate> getLastTrainingDate();


    /**
     * Returns the lesson with the lowest KnowledgeLevel
     *
     * @return LessonKnowledgeLevel holding the result wrapped in LiveData.
     */
    @Query("SELECT lessonName,avg(knowledgeLevel) AS average " +
            "FROM lesson JOIN lesson_mapping ON lesson.id=lesson_mapping.lessonEntryId " +
            "JOIN library ON lesson_mapping.libraryEntryId = library.id " +
            "WHERE lesson.language = (SELECT language FROM language WHERE isActive=1 LIMIT 1)" +
            "GROUP BY lessonName " +
            "ORDER BY avg(knowledgeLevel) ASC " +
            "LIMIT 1")
    LiveData<LessonKnowledgeLevel> getWorstLesson();
}