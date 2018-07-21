package com.sliebald.cula.ui.training;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sliebald.cula.R;
import com.sliebald.cula.data.CulaRepository;
import com.sliebald.cula.data.database.Entities.LibraryEntry;
import com.sliebald.cula.data.database.Entities.StatisticEntry;
import com.sliebald.cula.databinding.ActivityTrainingBinding;
import com.sliebald.cula.ui.startTraining.StartTrainingFragment;
import com.sliebald.cula.utilities.InjectorUtils;
import com.sliebald.cula.utilities.KnowledgeLevelUtils;

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
        mCulaRepository = InjectorUtils.provideRepository();

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
        TrainingViewModelFactory viewModelFactory = new TrainingViewModelFactory(amountOfWords,
                minKnowledgeLevel, maxKnowledgeLevel, lessonId);
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(TrainingViewModel.class);
        mViewModel.getEntries().observe(this, libraryEntries -> {
            if (libraryEntries != null) {
                //we don't need updates during training.
                mViewModel.getEntries().removeObservers(this);
                if (libraryEntries.size() > 0) {
                    Log.d("TEST", "size: " + libraryEntries.size() + " entry 1: " +
                            libraryEntries.get(0).toString());
                    showNextWord();
                } else {
                    Toast.makeText(this, R.string.start_training_warning_no_matching_words, Toast
                            .LENGTH_LONG)
                            .show();
                }
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
            mBinding.tvLabelProgress.setText(getString(R.string.activity_training_label_progress,
                    mViewModel.getLearningSetPosition(), mViewModel.getLearningSetSize()));
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
        LibraryEntry currentEntry = mViewModel.getCurrentWord();
        String correctTranslation;
        if (!reverseTraining) {
            correctTranslation = currentEntry.getForeignWord().trim().toLowerCase();
        } else
            correctTranslation = currentEntry.getNativeWord().trim().toLowerCase();


        double updatedKnowledgeLevel;
        boolean trainingCorrect = typedTranslation.equals(correctTranslation);

        if (trainingCorrect) {
            Toast.makeText(this, "correct", Toast.LENGTH_SHORT).show();
            updatedKnowledgeLevel = KnowledgeLevelUtils.calculateKnowlevelAdjustment(currentEntry
                    .getKnowledgeLevel(), true);
            mCulaRepository.insertStatisticsEntry(new StatisticEntry(currentEntry.getId(),
                    null, 1));
        } else {
            Toast.makeText(this, "wrong: " + typedTranslation + " " + correctTranslation, Toast
                    .LENGTH_SHORT).show();
            updatedKnowledgeLevel = KnowledgeLevelUtils.calculateKnowlevelAdjustment(currentEntry
                    .getKnowledgeLevel(), false);
            mCulaRepository.insertStatisticsEntry(new StatisticEntry(currentEntry.getId(),
                    null, 0));
        }
        currentEntry.setKnowledgeLevel(updatedKnowledgeLevel);

        mCulaRepository.updateLibraryEntry(currentEntry);

        showNextWord();
    }
}