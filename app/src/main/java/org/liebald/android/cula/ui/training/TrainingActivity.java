package org.liebald.android.cula.ui.training;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.liebald.android.cula.R;
import org.liebald.android.cula.data.CulaRepository;
import org.liebald.android.cula.databinding.ActivityTrainingBinding;
import org.liebald.android.cula.utilities.InjectorUtils;

public class TrainingActivity extends AppCompatActivity {


    /**
     * The databinding for the Layout.
     */
    private ActivityTrainingBinding mBinding;

    /**
     * The {@link CulaRepository} that provides access to all data sources.
     */
    private CulaRepository mCulaRepository;

    private TrainingViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create DataBinding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_training);

        // get DB access
        mCulaRepository = InjectorUtils.provideRepository(this);

        //set back button
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

        // get the intent that started the learning activity.
        Intent intent = getIntent();


        //create the view model
        TrainingViewModelFactory viewModelFactory = new TrainingViewModelFactory(mCulaRepository,
                1, 0, 5, 2);
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(TrainingViewModel.class);
        mViewModel.getEntries().observe(this, libraryEntries -> {
            if (libraryEntries != null) {
                if (libraryEntries.size() > 0)
                    Log.d("TEST", "size: " + libraryEntries.size() + " entry 1: " +
                            libraryEntries.get(0).toString());
                else Log.d("TEST", "size: 0");
            }
        });

    }





}
