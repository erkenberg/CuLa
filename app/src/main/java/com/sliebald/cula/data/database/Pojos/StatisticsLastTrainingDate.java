package com.sliebald.cula.data.database.Pojos;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

import java.util.Date;


public class StatisticsLastTrainingDate {

    @ColumnInfo(name = "lastActive")
    public Date lastActive;

    @NonNull
    @Override
    public String toString() {
        return "StatisticsLastTrainingDate{" +
                "lastActive=" + lastActive +
                '}';
    }
}
