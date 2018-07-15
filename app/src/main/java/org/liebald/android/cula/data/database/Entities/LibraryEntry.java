package org.liebald.android.cula.data.database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

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
     * The level of the {@link LibraryEntry}.
     */
    private double knowledgeLevel;

    /**
     * Timestamp when the {@link LibraryEntry} was last updated.
     */
    @NonNull
    private Date lastUpdated;


    /**
     * Constructor for an {@link LibraryEntry}.
     *
     * @param id          The Id of the @{@link LibraryEntry}.
     * @param nativeWord  The stored native language word.
     * @param foreignWord The translation of the word in the foreign language.
     * @param language The language this {@link LibraryEntry} belongs to.
     * @param knowledgeLevel The level of this {@link LibraryEntry}.
     * @param lastUpdated Timestamp of the last update of the {@link LibraryEntry}
     */
    public LibraryEntry(int id, @NonNull String nativeWord, @NonNull String foreignWord, @NonNull
            String language, double knowledgeLevel, @NonNull Date lastUpdated) {
        this(nativeWord, foreignWord, language, knowledgeLevel);
        this.id = id;
        this.lastUpdated = lastUpdated;
    }

    /**
     * Constructor for an {@link LibraryEntry}.
     *
     * @param nativeWord  The stored native language word.
     * @param foreignWord The translation of the word in the foreign language.
     * @param language The language this {@link LibraryEntry} belongs to.
     * @param knowledgeLevel The level of this {@link LibraryEntry}.
     */
    @Ignore
    public LibraryEntry(@NonNull String nativeWord, @NonNull String foreignWord, @NonNull String language, double knowledgeLevel) {
        this.nativeWord = nativeWord;
        this.foreignWord = foreignWord;
        this.knowledgeLevel = knowledgeLevel;
        this.language = language;
        lastUpdated = new Date();
    }

    /**
     * Constructor for an {@link LibraryEntry}.
     *
     * @param nativeWord     The stored native language word.
     * @param foreignWord    The translation of the word in the foreign language.
     * @param language       The language this {@link LibraryEntry} belongs to.
     * @param knowledgeLevel The level of this {@link LibraryEntry}.
     * @param lastUpdated    Timestamp of the last update of the {@link LibraryEntry}
     */
    @Ignore
    public LibraryEntry(@NonNull String nativeWord, @NonNull String foreignWord, @NonNull String
            language, double knowledgeLevel, @NonNull Date lastUpdated) {
        this.nativeWord = nativeWord;
        this.foreignWord = foreignWord;
        this.knowledgeLevel = knowledgeLevel;
        this.language = language;
        this.lastUpdated = lastUpdated;
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
     * Setter for the knowledge level of the {@link LibraryEntry}.
     *
     * @param knowledgeLevel knowledge level of the {@link LibraryEntry}.
     */
    public void setKnowledgeLevel(double knowledgeLevel) {
        this.knowledgeLevel = knowledgeLevel;
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


    /**
     * Get the date of the last update of the {@link LibraryEntry}-
     *
     * @return Date of the last update.
     */
    @NonNull
    public Date getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Setter for the update time of the {@link LibraryEntry}.
     *
     * @param lastUpdated time of the last update of {@link LibraryEntry}.
     */
    public void setLastUpdated(@NonNull Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "LibraryEntry{" +
                "id=" + id +
                ", nativeWord='" + nativeWord + '\'' +
                ", foreignWord='" + foreignWord + '\'' +
                ", language='" + language + '\'' +
                ", level=" + knowledgeLevel +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}