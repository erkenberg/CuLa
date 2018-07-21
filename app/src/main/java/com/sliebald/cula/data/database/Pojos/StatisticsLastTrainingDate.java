package com.sliebald.cula.data.database.Pojos;

import android.arch.persistence.room.ColumnInfo;

import java.util.Date;


public class StatisticsLastTrainingDate {

    @ColumnInfo(name = "lastActive")
    public Date lastActive;

    public Date getLastActive() {
        return lastActive;
    }

}
