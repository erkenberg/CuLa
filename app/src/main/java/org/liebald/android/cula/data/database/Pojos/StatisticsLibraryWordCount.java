package org.liebald.android.cula.data.database.Pojos;

import android.arch.persistence.room.ColumnInfo;


public class StatisticsLibraryWordCount {

    /**
     * KnowledgeLevel aggregation (0-1 is 1, 1-2 is 2,...).
     */
    @ColumnInfo(name = "level")
    public double level;

    /**
     * How many LibraryEntries are in that range of the KnowledgeLevel.
     */
    @ColumnInfo(name = "count")
    public int count;

    public double getLevel() {
        return level;
    }

    public int getCount() {
        return count;
    }
}
