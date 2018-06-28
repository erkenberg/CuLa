package org.liebald.android.cula.data.database.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import org.liebald.android.cula.data.database.CulaDatabase;
import org.liebald.android.cula.data.database.Entities.LessonEntry;
import org.liebald.android.cula.data.database.Entities.LessonMappingEntry;

import java.util.List;

/**
 * {@link Dao} which provides an api for all data operations with the {@link CulaDatabase} related to the lessons.
 */
@Dao
public interface LessonDao {

    /**
     * Inserts a {@link LessonEntry} into the lessons table. If there is a conflicting id the
     * {@link LessonEntry} uses the {@link OnConflictStrategy} to abort if an according entry already exists.
     * The required uniqueness of these values is defined in the {@link LessonEntry}.
     *
     * @param lessonEntries A list of {@link LessonEntry}s to insert
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertEntry(LessonEntry... lessonEntries);

    /**
     * Inserts a {@link LessonMappingEntry} into the lesson_mapping table. If there is a conflicting id the
     * {@link LessonMappingEntry} uses the {@link OnConflictStrategy} to abort if an according entry already exists.
     * The required uniqueness of these values is defined in the {@link LessonMappingEntry}.
     *
     * @param lessonMappingEntries A list of {@link LessonMappingEntry}s to insert
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertEntry(LessonMappingEntry... lessonMappingEntries);

    /**
     * Gets all {@link LessonEntry}s in the lesson database table.
     *
     * @return {@link LiveData} with all @{@link LessonEntry}s.
     */
    @Query("SELECT id,lessonName,lessonDescription FROM lesson ORDER BY lessonName")
    LiveData<List<LessonEntry>> getAllEntries();


    /**
     * Get the current amount of entries in the lesson table.
     *
     * @return The current size.
     */
    @Query("Select count(id) from lesson")
    int getAmountOfLessons();

    /**
     * Get the current amount of entries in the lesson_mapping table.
     *
     * @return The current size.
     */
    @Query("Select count(id) from lesson_mapping")
    int getAmountOfLessonsMappings();

    /**
     * Delete the given {@link LessonEntry} from the lesson table.
     */
    @Delete
    void deleteEntry(LessonEntry entry);
}