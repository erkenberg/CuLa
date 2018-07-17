package org.liebald.android.cula.data.database.Pojos;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Ignore;


public class StatisticsActivityEntry {

    /**
     * KnowledgeLevel aggregation (0-1 is 1, 1-2 is 2,...).
     */
    @ColumnInfo(name = "date")
    public String date;

    /**
     * How many LibraryEntries are in that range of the KnowledgeLevel.
     */
    @ColumnInfo(name = "activity")
    public int activity;

    public String getDate() {
        return date;
    }

    public int getActivity() {
        return activity;
    }

    @Ignore
    @Override
    public String toString() {
        return "StatisticsActivityEntry{" +
                "date=" + date +
                ", activity=" + activity +
                '}';
    }
}
