package org.liebald.android.cula.services;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.liebald.android.cula.data.database.Entities.QuoteEntry;
import org.liebald.android.cula.utilities.AppExecutors;
import org.liebald.android.cula.utilities.InjectorUtils;
import org.liebald.android.cula.utilities.JsonUtils;
import org.liebald.android.cula.utilities.NetworkUtils;

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
                InjectorUtils.provideRepository(getApplicationContext()).setQuoteEntry(quoteEntry);
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
