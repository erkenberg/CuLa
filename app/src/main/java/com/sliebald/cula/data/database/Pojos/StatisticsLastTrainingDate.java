package com.sliebald.cula.data.database.Pojos;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;


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
