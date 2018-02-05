package android.liebald.com.cula.data.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * An @{@link Entity} Describing a word pair.
 */
@Entity(tableName = "dictionary")
public class DictionaryEntry {

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
     * Constructor for an DictionaryEntry.
     *
     * @param id          The Id of the @{@link DictionaryEntry}.
     * @param nativeWord  The stored native language word.
     * @param foreignWord The translation of the word in the foreign language.
     */
    public DictionaryEntry(int id, @NonNull
            String nativeWord, @NonNull
                                   String foreignWord) {
        this.id = id;
        this.nativeWord = nativeWord;
        this.foreignWord = foreignWord;
    }

    /**
     * Getter for the id of this {@link DictionaryEntry}.
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

}