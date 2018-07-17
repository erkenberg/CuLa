package org.liebald.android.cula.data.database.Pojos;

import android.arch.persistence.room.ColumnInfo;

import org.liebald.android.cula.data.database.Entities.LibraryEntry;

/**
 * Simple POJO that contains the required data of a {@link LibraryEntry} to map it to a lesson.
 * LessonID not stored, since only entries for a single lesson will be queried at a time and the
 * lessonID is therefore implicit.
 */
public class MappingPOJO {

    /**
     * Id of the {@link LibraryEntry}.
     */
    @ColumnInfo(name = "id")
    public int libraryId;

    /**
     * Native word of the {@link LibraryEntry}.
     */
    @ColumnInfo(name = "nativeWord")
    public String native_word;

    /**
     * Foreign word of the {@link LibraryEntry}.
     */
    @ColumnInfo(name = "foreignWord")
    public String foreign_word;

    /**
     * Info whether the {@link LibraryEntry} is part of the lesson that was queried.
     */
    @ColumnInfo(name = "partOfLesson")
    public boolean partOfLesson;

    @Override
    public String toString() {
        return "MappingPOJO{" +
                "libraryId=" + libraryId +
                ", native_word='" + native_word + '\'' +
                ", foreign_word='" + foreign_word + '\'' +
                ", partOfLesson=" + partOfLesson +
                '}';
    }

    public int getLibraryId() {
        return libraryId;
    }

    public String getNative_word() {
        return native_word;
    }

    public String getForeign_word() {
        return foreign_word;
    }

    public boolean isPartOfLesson() {
        return partOfLesson;
    }
}
