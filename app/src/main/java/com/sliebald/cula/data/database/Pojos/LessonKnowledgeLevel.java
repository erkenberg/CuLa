package com.sliebald.cula.data.database.Pojos;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;


/**
 * Simple POJO class that holds a lesson and the average of Knowledge Levels of all words in that
 * lesson.
 */
public class LessonKnowledgeLevel {

    /**
     * Lesson Name.
     */
    @ColumnInfo(name = "lessonName")
    public String lessonName;

    /**
     * Average of KnowledgeLevel of all
     * {@link com.sliebald.cula.data.database.Entities.LibraryEntry}s assigned to the
     * lesson.
     */
    @ColumnInfo(name = "average")
    public double average;

    @NonNull
    @Override
    public String toString() {
        return "LessonKnowledgeLevel{" +
                "lessonName='" + lessonName + '\'' +
                ", average=" + average +
                '}';
    }
}
