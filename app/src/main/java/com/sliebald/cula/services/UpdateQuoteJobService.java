package com.sliebald.cula.services;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.sliebald.cula.data.database.Entities.QuoteEntry;
import com.sliebald.cula.utilities.AppExecutors;
import com.sliebald.cula.utilities.InjectorUtils;
import com.sliebald.cula.utilities.JsonUtils;
import com.sliebald.cula.utilities.NetworkUtils;

import java.io.IOException;
import java.util.concurrent.Executor;

public class UpdateQuoteJobService extends JobService {

    /**
     * Tag for logging.
     */
    private static final String TAG = UpdateQuoteJobService.class.getSimpleName();

    @Override
    public boolean onStartJob(JobParameters job) {
        Log.d(TAG, "Starting update for Quote of the Day");
        Executor executor = AppExecutors.getInstance().networkIO();
        executor.execute(() -> {
            String jsonQuote = null;
            try {
                jsonQuote = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.getQuoteOfTheDayUrl());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (jsonQuote != null) {
                QuoteEntry quoteEntry = JsonUtils.parseQuoteJson(jsonQuote);
                InjectorUtils.provideRepository().insertQuoteEntry
                        (quoteEntry);
                jobFinished(job, false);
            } else {
                jobFinished(job, true);
            }
        });

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.d(TAG, "OnStop was called");
        return true;
    }
}
