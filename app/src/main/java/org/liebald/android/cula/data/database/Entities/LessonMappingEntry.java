package org.liebald.android.cula.data.database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * An @{@link Entity} Describing a mapping between a {@link LessonEntry} and a {@link LibraryEntry}.
 */
//TODO: foreign keys.
@Entity(tableName = "library")
public class LessonMappingEntry {

    /**
     * The Id of the Entry in the Database.
     */
    @PrimaryKey(autoGenerate = true)
    private int id;

    /**
     * The id of the {@link LibraryEntry} mapped to the {@link LessonEntry}.
     */
    private int libraryId;

    /**
     * The id of the {@link LessonEntry} mapped to a {@link LibraryEntry}.
     */
    private int lessonId;


    /**
     * Constructor for an {@link LessonMappingEntry}.
     *
     * @param id        The Id of the @{@link LessonMappingEntry}.
     * @param libraryId The id of the {@link LibraryEntry} mapped to the {@link LessonEntry}.
     * @param lessonId  The id of the {@link LessonEntry} mapped to a {@link LibraryEntry}.
     */
    public LessonMappingEntry(int id, int libraryId, int lessonId) {
        this(libraryId, lessonId);
        this.id = id;
    }

    /**
     * Constructor for an {@link LessonMappingEntry}.
     *
     * @param libraryId The id of the {@link LibraryEntry} mapped to the {@link LessonEntry}.
     * @param lessonId  The id of the {@link LessonEntry} mapped to a {@link LibraryEntry}.
     */
    @Ignore
    public LessonMappingEntry(int libraryId, int lessonId) {
        this.libraryId = libraryId;
        this.lessonId = lessonId;
    }

    /**
     * Getter for the id of this {@link LessonMappingEntry}.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for the id of the {@link LibraryEntry} mapped to the {@link LessonEntry}.
     *
     * @return The {@link LibraryEntry} id.
     */
    public int getLibraryId() {
        return libraryId;
    }

    /**
     * Getter for the id of the {@link LessonEntry} mapped to a {@link LibraryEntry}.
     *
     * @return The {@link LessonEntry} id.
     */
    public int getLessonId() {
        return lessonId;
    }


    @Override
    public String toString() {
        return "LessonMappingEntry{" +
                "id=" + id +
                ", libraryId=" + libraryId +
                ", lessonId=" + lessonId +
                '}';
    }
}