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
     * Constructor for an {@link LanguageEntry}.
     *
     * @param language The stored native language word.
     */
    public LanguageEntry(@NonNull String language) {
        this.language = StringUtils.toFirstCharacterUpperCase(language);
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