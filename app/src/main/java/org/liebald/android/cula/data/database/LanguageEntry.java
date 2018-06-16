package org.liebald.android.cula.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * An @{@link Entity} Describing a foreign language that can be trained.
 */
@Entity(tableName = "language")
public class LanguageEntry {

    /**
     * The Id of the foreign language in the Database.
     */
    @PrimaryKey(autoGenerate = true)
    private int id;

    /**
     * The foreign language.
     */
    @NonNull
    private String language;

    /**
     * Constructor for an {@link LanguageEntry}.
     *
     * @param id       The Id of the @{@link LanguageEntry}.
     * @param language The stored native language word.
     */
    public LanguageEntry(int id, @NonNull String language) {
        this.id = id;
        this.language = language;
    }

    /**
     * Constructor for an {@link LanguageEntry}.
     *
     * @param language The stored native language word.
     */
    @Ignore
    public LanguageEntry(@NonNull String language) {
        this.language = language;
    }

    /**
     * Getter for the id of this {@link LanguageEntry}.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for the foreign language.
     *
     * @return The foreign language.
     */
    @NonNull
    public String getLanguage() {
        return language;
    }


}