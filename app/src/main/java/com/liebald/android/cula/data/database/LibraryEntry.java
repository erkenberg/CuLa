package com.liebald.android.cula.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * An @{@link Entity} Describing a word pair.
 */
@Entity(tableName = "library")
public class LibraryEntry {

    /**
     * The Id of the Entry in the Database.
     */
    @PrimaryKey(autoGenerate = true)
    private int id;

    /**
     * The word in the native language.
     */
    @NonNull
    private String nativeWord;

    /**
     * The word in the foreign language.
     */
    @NonNull
    private String foreignWord;

    /**
     * The knowledgeLevel of the {@link LibraryEntry}.
     */
    private double knowledgeLevel;

    /**
     * Constructor for an {@link LibraryEntry}.
     *
     * @param id          The Id of the @{@link LibraryEntry}.
     * @param nativeWord  The stored native language word.
     * @param foreignWord The translation of the word in the foreign language.
     */
    public LibraryEntry(int id, @NonNull String nativeWord, @NonNull String foreignWord, double knowledgeLevel) {
        this.id = id;
        this.nativeWord = nativeWord;
        this.foreignWord = foreignWord;
        this.knowledgeLevel = knowledgeLevel;
    }

    /**
     * Constructor for an {@link LibraryEntry}.
     *
     * @param nativeWord  The stored native language word.
     * @param foreignWord The translation of the word in the foreign language.
     */
    @Ignore
    public LibraryEntry(@NonNull String nativeWord, @NonNull String foreignWord, double knowledgeLevel) {
        this.nativeWord = nativeWord;
        this.foreignWord = foreignWord;
        this.knowledgeLevel = knowledgeLevel;
    }

    /**
     * Getter for the id of this {@link LibraryEntry}.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for the native language word.
     *
     * @return The native language word.
     */
    @NonNull
    public String getNativeWord() {
        return nativeWord;
    }

    /**
     * Getter for the foreign language word.
     *
     * @return The foreign language word.
     */
    @NonNull
    public String getForeignWord() {
        return foreignWord;
    }

    /**
     * Getter for the knowledge level of the {@link LibraryEntry}.
     *
     * @return knowlede level of the {@link LibraryEntry}.
     */
    public double getKnowledgeLevel() {
        return knowledgeLevel;
    }

    @Override
    public String toString() {
        return "LibraryEntry{" +
                "id=" + id +
                ", nativeWord='" + nativeWord + '\'' +
                ", foreignWord='" + foreignWord + '\'' +
                ", knowledgeLevel=" + knowledgeLevel +
                '}';
    }
}