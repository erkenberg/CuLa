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
 * An @{@link Entity} Describing Statistic resulting from training.
 */
@Entity(tableName = "statistics",
        foreignKeys = {
                @ForeignKey(entity = LessonEntry.class,
                        parentColumns = "id",
                        childColumns = "lessonEntryID"),
                @ForeignKey(entity = LibraryEntry.class,
                        parentColumns = "id",
                        childColumns = "libraryEntryID",
                        onDelete = CASCADE,
                        onUpdate = CASCADE)
        },
        indices = {
                @Index(value = {"lessonEntryID"}),
                @Index(value = {"libraryEntryID"})
        })
public class StatisticEntry {

    /**
     * The Id of the Entry in the Database.
     */
    @PrimaryKey(autoGenerate = true)
    private int id;


    /**
     * The id of the {@link LibraryEntry} that was trained
     */
    private int libraryEntryID;

    /**
     * The id of the {@link LessonEntry} of the lesson that was trained.
     */
    private Integer lessonEntryID;


    /**
     * Specifies how successful was the training. Can take values between 0 and 1.
     */
    private double successRate;

    /**
     * Timestamp when the {@link StatisticEntry} was last updated.
     */
    @NonNull
    private Date trainingDate;


    /**
     * Constructor for an {@link StatisticEntry}.
     *
     * @param id             The Id of the @{@link StatisticEntry}.
     * @param libraryEntryID The id of the {@link LibraryEntry} that was trained
     * @param lessonEntryID  The id of the {@link LessonEntry} of the lesson that was trained.
     * @param successRate    Specifies how successful was the training. Can take values between 0
     *                       and 1.
     * @param trainingDate   The date of the training session.
     */
    public StatisticEntry(int id, int libraryEntryID, Integer lessonEntryID, double
            successRate, @NonNull Date trainingDate) {
        this(libraryEntryID, lessonEntryID, successRate);
        this.id = id;
        this.trainingDate = trainingDate;
    }

    /**
     * Constructor for an {@link StatisticEntry}.
     *
     * @param libraryEntryID The id of the {@link LibraryEntry} that was trained
     * @param lessonEntryID  The id of the {@link LessonEntry} of the lesson that was trained.
     * @param successRate    Specifies how successful was the training. Can take values between 0
     *                       and 1. If greater or smaller automatically rounded to the nearer
     *                       value. 1 means successful/correct trained, 0 means wrong.
     *                       intermediate values reserved for future use.
     */
    @Ignore
    public StatisticEntry(int libraryEntryID, Integer lessonEntryID, double successRate) {
        this.libraryEntryID = libraryEntryID;
        this.lessonEntryID = lessonEntryID;
        if (successRate < 0)
            this.successRate = 0;
        else if (successRate > 1)
            this.successRate = 1;
        else
            this.successRate = successRate;
        trainingDate = new Date();
    }

    /**
     * Getter for the id of this {@link StatisticEntry}.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }


    /**
     * Get the date of the last update of the {@link StatisticEntry}-
     *
     * @return Date of the last update.
     */
    @NonNull
    public Date getTrainingDate() {
        return trainingDate;
    }

    /**
     * Returns the {@link LibraryEntry} ID to which this {@link StatisticEntry} belongs.
     *
     * @return The {@link LibraryEntry} ID
     */
    public int getLibraryEntryID() {
        return libraryEntryID;
    }

    /**
     * Returns the {@link LessonEntry} ID of the lesson to which this {@link StatisticEntry}
     * belongs.
     *
     * @return The {@link LessonEntry} ID
     */
    public Integer getLessonEntryID() {
        return lessonEntryID;
    }

    /**
     * Getter for the successive of this {@link StatisticEntry}.
     *
     * @return The Success rate.
     */
    public double getSuccessRate() {
        return successRate;
    }
}