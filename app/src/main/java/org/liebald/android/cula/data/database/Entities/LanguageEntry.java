package org.liebald.android.cula.data.database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.liebald.android.cula.utilities.StringUtils;

/**
 * An @{@link Entity} Describing a foreign language that can be trained.
 */
@Entity(tableName = "language")
public class LanguageEntry {

    /**
     * The foreign language.
     */
    @PrimaryKey
    @NonNull
    private String language;

    /**
     * Defines whether this entry is the currently active entry.
     */
    private boolean isActive;

    /**
     * Constructor for an {@link LanguageEntry}.
     *
     * @param language The stored native language word.
     * @param isActive Defines whether this entry is the currently active entry.
     */
    public LanguageEntry(@NonNull String language, boolean isActive) {
        this.language = StringUtils.toFirstCharacterUpperCase(language);
        this.isActive = isActive;
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

    /**
     * Defines whether this entry is the currently active entry.
     */
    public boolean isActive() {
        return isActive;
    }

}