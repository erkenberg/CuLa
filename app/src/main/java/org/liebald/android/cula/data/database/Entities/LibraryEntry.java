package org.liebald.android.cula.data.database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * An @{@link Entity} Describing a word pair.
 */
@Entity(tableName = "library",
        foreignKeys = @ForeignKey(entity = LanguageEntry.class,
                parentColumns = "language",
                childColumns = "language",
                onDelete = CASCADE,
                onUpdate = CASCADE),
        indices = {@Index(value = {"language"})})
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
     * To which language as described in an {@link LanguageEntry} does this word belong.
     */
    @NonNull
    private String language;

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
    public LibraryEntry(int id, @NonNull String nativeWord, @NonNull String foreignWord, @NonNull String language, double knowledgeLevel) {
        this(nativeWord, foreignWord, language, knowledgeLevel);
        this.id = id;
    }

    /**
     * Constructor for an {@link LibraryEntry}.
     *
     * @param nativeWord  The stored native language word.
     * @param foreignWord The translation of the word in the foreign language.
     */
    @Ignore
    public LibraryEntry(@NonNull String nativeWord, @NonNull String foreignWord, @NonNull String language, double knowledgeLevel) {
        this.nativeWord = nativeWord;
        this.foreignWord = foreignWord;
        this.knowledgeLevel = knowledgeLevel;
        this.language = language;
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
     * @return knowledge level of the {@link LibraryEntry}.
     */
    public double getKnowledgeLevel() {
        return knowledgeLevel;
    }

    /**
     * To which language as described in an {@link LanguageEntry} does this word belong.
     *
     * @return The language as word.
     */
    @NonNull
    public String getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return "LibraryEntry{" +
                "id=" + id +
                ", nativeWord='" + nativeWord + '\'' +
                ", foreignWord='" + foreignWord + '\'' +
                ", language='" + language + '\'' +
                ", knowledgeLevel=" + knowledgeLevel +
                '}';

    }
}