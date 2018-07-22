package com.sliebald.cula;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;

public class Analytics {


    public static void logEventStartTraining(Context context) {
        FirebaseAnalytics.getInstance(context).logEvent("start_training", null);
    }

    public static void logEventStopTraining(Context context) {
        FirebaseAnalytics.getInstance(context).logEvent("start_training", null);
    }
}
