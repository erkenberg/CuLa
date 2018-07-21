package com.sliebald.android.cula.data.database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * An @{@link Entity} Describing a mapping between a {@link LessonEntry} and a {@link LibraryEntry}.
 */
@Entity(tableName = "lesson_mapping",
        foreignKeys = {
                @ForeignKey(entity = LessonEntry.class,
                        parentColumns = "id",
                        childColumns = "lessonEntryId",
                        onDelete = CASCADE,
                        onUpdate = CASCADE),
                @ForeignKey(entity = LibraryEntry.class,
                        parentColumns = "id",
                        childColumns = "libraryEntryId",
                        onDelete = CASCADE,
                        onUpdate = CASCADE)
        },
        indices = {
                @Index(value = {"lessonEntryId"}),
                @Index(value = {"libraryEntryId"})
        })
public class LessonMappingEntry {

    /**
     * The Id of the Entry in the Database.
     */
    @PrimaryKey(autoGenerate = true)
    private int id;

    /**
     * The id of the {@link LibraryEntry} mapped to the {@link LessonEntry}.
     */
    private int libraryEntryId;

    /**
     * The id of the {@link LessonEntry} mapped to a {@link LibraryEntry}.
     */
    private int lessonEntryId;


    /**
     * Constructor for an {@link LessonMappingEntry}.
     *
     * @param id             The Id of the @{@link LessonMappingEntry}.
     * @param lessonEntryId  The id of the {@link LessonEntry} mapped to a {@link LibraryEntry}.
     * @param libraryEntryId The id of the {@link LibraryEntry} mapped to the {@link LessonEntry}.
     */
    public LessonMappingEntry(int id, int lessonEntryId, int libraryEntryId) {
        this(lessonEntryId, libraryEntryId);
        this.id = id;
    }

    /**
     * Constructor for an {@link LessonMappingEntry}.
     *
     * @param lessonEntryId  The id of the {@link LessonEntry} mapped to a {@link LibraryEntry}.
     * @param libraryEntryId The id of the {@link LibraryEntry} mapped to the {@link LessonEntry}.
     */
    @Ignore
    public LessonMappingEntry(int lessonEntryId, int libraryEntryId) {
        this.libraryEntryId = libraryEntryId;
        this.lessonEntryId = lessonEntryId;
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
    public int getLibraryEntryId() {
        return libraryEntryId;
    }

    /**
     * Getter for the id of the {@link LessonEntry} mapped to a {@link LibraryEntry}.
     *
     * @return The {@link LessonEntry} id.
     */
    public int getLessonEntryId() {
        return lessonEntryId;
    }


    @Override
    public String toString() {
        return "LessonMappingEntry{" +
                "id=" + id +
                ", libraryEntryId=" + libraryEntryId +
                ", lessonEntryId=" + lessonEntryId +
                '}';
    }
}