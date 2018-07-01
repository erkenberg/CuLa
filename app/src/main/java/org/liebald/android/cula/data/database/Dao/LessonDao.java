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
import org.liebald.android.cula.data.database.Entities.MappingPOJO;

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
    @Query("SELECT id, lessonName, lessonDescription, language FROM lesson WHERE language=:language ORDER BY lessonName DESC")
    LiveData<List<LessonEntry>> getAllEntries(String language);


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

    /**
     * Gets the {@link LessonEntry} in the lesson database table with the given id.
     *
     * @param id The id of the entry.
     * @return {@link LiveData} with the @{@link LessonEntry}.
     */
    @Query("SELECT id, lessonName, lessonDescription, language FROM lesson WHERE id=:id")
    LiveData<LessonEntry> getEntryById(int id);


    /**
     * Gets the List of {@link MappingPOJO}s in the lesson database table with the given id.
     *
     * @param id The lesson id for which the {@link List} of {@link MappingPOJO}s should be retrieved.
     * @return {@link LiveData} with the {@link List} of @{@link MappingPOJO}s.
     */
    //TODO: change default 1 to correct boolean indicating whether an entry belings the the given lesson
    @Query("SELECT id, foreignWord, nativeWord, 1 as partOfLesson FROM library WHERE id=:id or 1=1")
    LiveData<List<MappingPOJO>> getLessonMappingById(int id);

}
