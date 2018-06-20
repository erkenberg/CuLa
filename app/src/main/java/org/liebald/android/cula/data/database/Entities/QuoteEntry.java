package org.liebald.android.cula.data.database.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * POJO object to store a quote.
 */
@Entity(tableName = "quotes")
public class QuoteEntry {

    /**
     * The text of the {@link QuoteEntry}.
     */
    private final String text;
    /**
     * The Id of the {@link QuoteEntry} in the Database.
     */
    @PrimaryKey(autoGenerate = true)
    private int id;
    /**
     * Timestamp when the {@link QuoteEntry} was created in the Database.
     */
    @NonNull
    private Date createdAt;

    /**
     * Constructor for an {@link QuoteEntry}.
     *
     * @param text The stored text of the {@link QuoteEntry}.
     */
    @Ignore
    public QuoteEntry(@NonNull String text) {
        this.text = text;
        createdAt = new Date();
    }


    /**
     * Constructor for a new {@link QuoteEntry}.
     *
     * @param id   The ID of the quote.
     * @param text Text as String.
     */
    public QuoteEntry(int id, String text) {
        this(text);
        this.id = id;
    }

    /**
     * Returns the text of the {@link QuoteEntry}.
     *
     * @return The text as String.
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the ID of the {@link QuoteEntry}
     *
     * @return The id as int.
     */
    public int getId() {
        return id;
    }

    /**
     * The timestamp when the quote was created.
     *
     * @return The timestamp as {@link Date}
     */
    @NonNull
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@NonNull Date createdAt) {
        this.createdAt = createdAt;
    }
}
