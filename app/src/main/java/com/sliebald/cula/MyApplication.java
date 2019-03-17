package com.sliebald.cula;

import android.app.Application;

/**
 * Application class to provide access to the application context for e.g. the Database.
 * Based on https://stackoverflow.com/questions/21818905/get-application-context-from-non
 * -activity-singleton-class
 */
public class MyApplication extends Application {

    private static MyApplication mContext;

    /**
     * Get the Application context of the App.
     *
     * @return The {@link android.content.Context} of this app.
     */
    public static MyApplication getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}
