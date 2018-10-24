package com.sliebald.cula.data.database.Entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

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
     * The author of the {@link QuoteEntry}.
     */
    private final String author;

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
    public QuoteEntry(@NonNull String text, @NonNull String author) {
        this.text = text;
        this.author = author;
        createdAt = new Date();
    }


    /**
     * Constructor for a new {@link QuoteEntry}.
     *
     * @param id   The ID of the quote.
     * @param text Text as String.
     */
    public QuoteEntry(int id, String text, String author) {
        this(text, author);
        this.id = id;
    }

    /**
     * Returns the text of the {@link QuoteEntry}.
     *
     * @return The text as {@link String}.
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the Author of the {@link QuoteEntry}.
     *
     * @return The Author as {@link String}.
     */
    public String getAuthor() {
        return author;
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

    @Override
    public String toString() {
        return "QuoteEntry{" +
                "text='" + text + '\'' +
                ", author='" + author + '\'' +
                ", id=" + id +
                ", createdAt=" + createdAt +
                '}';
    }
}
