package org.liebald.android.cula.data.database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * An @{@link Entity} Describing a lesson.
 */
@Entity(tableName = "lesson",
        foreignKeys = @ForeignKey(entity = LanguageEntry.class,
                parentColumns = "language",
                childColumns = "language",
                onDelete = CASCADE,
                onUpdate = CASCADE),
        indices = {@Index(value = {"language"})})
public class LessonEntry {

    /**
     * The Id of the {@link LessonEntry} in the Database.
     */
    @PrimaryKey(autoGenerate = true)
    private int id;

    /**
     * The name of the lesson.
     */
    @NonNull
    private String lessonName;

    /**
     * The description of the lesson.
     */
    @NonNull
    private String lessonDescription;

    /**
     * To which language as described in an {@link LanguageEntry} does this word belong.
     */
    @NonNull
    private String language;

    /**
     * Constructor for an {@link LessonEntry}.
     *
     * @param id                The Id of the @{@link LessonEntry}.
     * @param lessonName        The name of the {@link LessonEntry}.
     * @param lessonDescription The description of the {@link LessonEntry}.
     * @param language          The language this {@link LessonEntry} belongs to.
     */
    public LessonEntry(int id, @NonNull String lessonName, @NonNull String lessonDescription,
                       @NonNull String language) {
        this(lessonName, lessonDescription, language);
        this.id = id;
    }

    /**
     * Constructor for an {@link LessonEntry}.
     *
     * @param lessonName        The name of the {@link LessonEntry}.
     * @param lessonDescription The description of the {@link LessonEntry}.
     * @param language          The language this {@link LessonEntry} belongs to.
     */
    @Ignore
    public LessonEntry(@NonNull String lessonName, @NonNull String lessonDescription, @NonNull
            String
            language) {
        this.lessonName = lessonName;
        this.lessonDescription = lessonDescription;
        this.language = language;
    }

    /**
     * Getter for the id of this {@link LessonEntry}.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for the name of the lesson.
     *
     * @return The name of the lesson.
     */
    @NonNull
    public String getLessonName() {
        return lessonName;
    }

    /**
     * Getter for the description of the lesson.
     *
     * @return The description of the lesson.
     */
    @NonNull
    public String getLessonDescription() {
        return lessonDescription;
    }

    /**
     * To which language as described in an {@link LanguageEntry} does this word belong.
     *
     * @return The language as String.
     */
    @NonNull
    public String getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return "LessonEntry{" +
                "id=" + id +
                ", lessonName='" + lessonName + '\'' +
                ", lessonDescription='" + lessonDescription + '\'' +
                '}';
    }
}