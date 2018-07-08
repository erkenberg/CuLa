package org.liebald.android.cula.ui.training;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.liebald.android.cula.R;
import org.liebald.android.cula.data.CulaRepository;
import org.liebald.android.cula.data.database.Entities.LibraryEntry;
import org.liebald.android.cula.databinding.ActivityTrainingBinding;
import org.liebald.android.cula.databinding.ActivityUpdateLibraryBinding;
import org.liebald.android.cula.ui.updateLibrary.UpdateLibraryViewModel;
import org.liebald.android.cula.ui.updateLibrary.UpdateLibraryViewModelFactory;
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


    }





}
