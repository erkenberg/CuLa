package org.liebald.android.cula.services;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.liebald.android.cula.data.database.Entities.QuoteEntry;
import org.liebald.android.cula.utilities.InjectorUtils;

public class UpdateQuoteJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters job) {
        InjectorUtils.provideRepository(getApplicationContext()).setQuoteEntry(new QuoteEntry("TEST SERVICE"));
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
