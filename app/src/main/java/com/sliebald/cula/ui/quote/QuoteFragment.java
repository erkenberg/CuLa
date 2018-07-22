package com.sliebald.cula.ui.quote;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.sliebald.cula.R;
import com.sliebald.cula.databinding.FragmentQuoteBinding;
import com.sliebald.cula.services.UpdateQuoteJobService;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuoteFragment extends Fragment {

    public static final String TAG = QuoteFragment.class.getSimpleName();


    private FragmentQuoteBinding mBinding;

    private QuoteViewModel mViewModel;


    public QuoteFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() == null || getContext() == null)
            return;
        mViewModel = ViewModelProviders.of(getActivity()).get(QuoteViewModel.class);

    }


    @Override
    public void onResume() {
        super.onResume();
        scheduleJobService();

    }

    /**
     * Schedules a Job service to regularly update the motivational quote.
     */
    private void scheduleJobService() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getContext()));

//         Update the database immediately, only added if this job isn't running already.
        Job myInstantJob = dispatcher.newJobBuilder()
                .setService(UpdateQuoteJobService.class)
                .setTag("updateQuoteJobService")
                .setRecurring(false)
                .build();
        dispatcher.mustSchedule(myInstantJob);

        // and also update it each 12-13 hours as recurring job, only added if this job isn't
        // running already.
        //TODO: Recurring doesn't seem to work properly
//        Job recurringJob = dispatcher.newJobBuilder()
//                .setService(UpdateQuoteJobService.class)
//                .setTag("updateQuoteJobService")
//                .setLifetime(Lifetime.FOREVER)
//                .setRecurring(true)
//                .setTrigger(Trigger.executionWindow(
//                        12 * 60 * 60,
//                        13 * 60 * 60))
//                .setReplaceCurrent(false)
//                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
//                .build();
//        dispatcher.mustSchedule(recurringJob);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_quote, container, false);

        mViewModel.getQuote().observe(this, quote -> {
            if (quote != null) {
                mBinding.quoteText.setText(quote.getText());
                mBinding.quoteAuthor.setText(quote.getAuthor());
            }
        });

        return mBinding.getRoot();
    }

}
