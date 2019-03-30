package com.sliebald.cula.data.database.Pojos;

import androidx.room.ColumnInfo;

import com.sliebald.cula.data.database.Entities.LibraryEntry;

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
    private int libraryId;

    /**
     * Native word of the {@link LibraryEntry}.
     */
    private String nativeWord;

    /**
     * Foreign word of the {@link LibraryEntry}.
     */
    private String foreignWord;

    /**
     * Info whether the {@link LibraryEntry} is part of the lesson that was queried.
     */
    @ColumnInfo(name = "partOfLesson")
    public boolean partOfLesson;

    /**
     * The Knowledge Level of the {@link LibraryEntry}.
     */
    @ColumnInfo(name = "knowledgeLevel")
    private double knowledgeLevel;




    public void setLibraryId(int libraryId) {
        this.libraryId = libraryId;
    }

    public void setNativeWord(String nativeWord) {
        this.nativeWord = nativeWord;
    }

    public void setForeignWord(String foreignWord) {
        this.foreignWord = foreignWord;
    }

    public void setPartOfLesson(boolean partOfLesson) {
        this.partOfLesson = partOfLesson;
    }

    public void setKnowledgeLevel(double knowledgeLevel) {
        this.knowledgeLevel = knowledgeLevel;
    }

    /**
     * Getter for the Id of the {@link LibraryEntry} linked to this entry.
     *
     * @return The Id of the {@link LibraryEntry}
     */
    public int getLibraryId() {
        return libraryId;
    }

    /**
     * Getter for the native Word of the {@link LibraryEntry} linked to this entry.
     *
     * @return The native Word of the {@link LibraryEntry}
     */
    public String getNativeWord() {
        return nativeWord;
    }

    /**
     * Getter for the foreign Word of the {@link LibraryEntry} linked to this entry.
     *
     * @return The foreign Word of the {@link LibraryEntry}
     */
    public String getForeignWord() {
        return foreignWord;
    }

    /**
     * Boolean that defines whether this entry is part of the lesson that was given to the query
     * returning a list of {@link MappingPOJO}s.
     *
     * @return True if part of that lesson, false otherwise.
     */
    public boolean isPartOfLesson() {
        return partOfLesson;
    }

    /**
     * Getter for the knowledge day of the {@link LibraryEntry}.
     *
     * @return knowledge day of the {@link LibraryEntry}.
     */
    public double getKnowledgeLevel() {
        return knowledgeLevel;
    }

    @Override
    public String toString() {
        return "MappingPOJO{" +
                "libraryId=" + libraryId +
                ", nativeWord='" + nativeWord + '\'' +
                ", foreignWord='" + foreignWord + '\'' +
                ", partOfLesson=" + partOfLesson +
                ", knowledgeLevel=" + knowledgeLevel +
                '}';
    }

}
