package org.liebald.android.cula.ui.training;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.liebald.android.cula.R;
import org.liebald.android.cula.data.CulaRepository;
import org.liebald.android.cula.data.database.Entities.LibraryEntry;
import org.liebald.android.cula.databinding.ActivityTrainingBinding;
import org.liebald.android.cula.ui.startTraining.StartTrainingFragment;
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
    boolean reverseTraining;
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

        // get the intent that started the learning activity and extract the relevant values.
        Intent intent = getIntent();
        int amountOfWords = intent.getIntExtra(StartTrainingFragment.BUNDLE_EXTRA_NUMWORDS_KEY,
                10);
        double minKnowledgeLevel = intent.getDoubleExtra(StartTrainingFragment
                .BUNDLE_EXTRA_KNOWLEDGE_LEVEL_MIN, 0);
        double maxKnowledgeLevel = intent.getDoubleExtra(StartTrainingFragment
                .BUNDLE_EXTRA_KNOWLEDGE_LEVEL_MAX, 5);
        int lessonId = intent.getIntExtra(StartTrainingFragment
                .BUNDLE_EXTRA_LESSON_KEY, -1);
        reverseTraining = intent.getBooleanExtra(StartTrainingFragment
                .BUNDLE_EXTRA_REVERSE_TRAINING_KEY, false);

        //create the view model
        TrainingViewModelFactory viewModelFactory = new TrainingViewModelFactory(mCulaRepository,
                amountOfWords, minKnowledgeLevel, maxKnowledgeLevel, lessonId);
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(TrainingViewModel.class);
        mViewModel.getEntries().observe(this, libraryEntries -> {
            if (libraryEntries != null) {
                //we don't need updates during training.
                mViewModel.getEntries().removeObservers(this);
                if (libraryEntries.size() > 0) {
                    Log.d("TEST", "size: " + libraryEntries.size() + " entry 1: " +
                            libraryEntries.get(0).toString());
                    showNextWord();
                } else Log.d("TEST", "size: 0");
            }
        });
    }

    /**
     * Display the next word to learn.
     */
    private void showNextWord() {
        if (!mViewModel.hasNextEntry()) {
            //TODO: show statistics or similar instead of just returning.
            finish();
        } else {
            mBinding.etTranslatedWord.getText().clear();
            mBinding.etTranslatedWord.requestFocus();
            LibraryEntry nextEntry = mViewModel.getNextEntry();
            if (!reverseTraining)
                mBinding.tvLabelWordToTranslate.setText(nextEntry.getNativeWord());
            else
                mBinding.tvLabelWordToTranslate.setText(nextEntry.getForeignWord());
        }
    }

    /**
     * Check the current word and move to the next.
     *
     * @param view The clicked check button.
     */
    public void checkWord(View view) {
        if (!mViewModel.hasNextEntry()) {
            //TODO: show statistics or similar instead of just returning.
            finish();
        }
        String typedTranslation = mBinding.etTranslatedWord.getText().toString().trim()
                .toLowerCase();
        String realTranslation;
        if (!reverseTraining) {
            realTranslation = mViewModel.getCurrentWord().getForeignWord().trim().toLowerCase();
        } else
            realTranslation = mViewModel.getCurrentWord().getNativeWord().trim().toLowerCase();


        //TODO: use snackbar
        //TODO: update word in database.
        if (typedTranslation.equals(realTranslation)) {
            Toast.makeText(this, "correct", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "wrong: " + typedTranslation + " " + realTranslation, Toast
                    .LENGTH_LONG)
                    .show();
        }
        showNextWord();
    }
}
