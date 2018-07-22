package com.sliebald.cula.ui.training;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

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
     * Tag of this activity.
     */
    private static final String TAG = TrainingActivity.class.getSimpleName();


    /**
     * The data binding for the Layout.
     */
    private ActivityTrainingBinding mBinding;

    /**
     * The {@link CulaRepository} that provides access to all data sources.
     */
    private CulaRepository mCulaRepository;

    /**
     * ViewModel of the activity.
     */
    private TrainingViewModel mViewModel;

    /**
     * Boolean defining whether reverse training should be done
     */
    private boolean reverseTraining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create DataBinding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_training);

        // get DB access
        mCulaRepository = InjectorUtils.provideRepository();
        Log.d(TAG, "SAVED STATE: " + savedInstanceState);
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
                    Log.d(TAG, "size: " + libraryEntries.size() + " entry 1: " +
                            libraryEntries.get(0).toString());
                    showWord(savedInstanceState == null);
                } else {
                    setResult(StartTrainingFragment.RESULT_KEY_NO_MATCHING_ENTRIES);
                    finish();
                }
            }
        });
    }

    /**
     * Display the next word to learn.
     * @param nextWord True if the next word should be displayed, false if the current one should
     *                be shown.
     */
    private void showWord(boolean nextWord) {
        if (!mViewModel.hasNextEntry()) {
            //TODO: show statistics or similar instead of just returning.
            finish();
        } else {
            mBinding.etTranslatedWord.getText().clear();
            mBinding.etTranslatedWord.requestFocus();
            LibraryEntry nextEntry;
            if (nextWord)
                nextEntry = mViewModel.getNextEntry();
            else
                nextEntry = mViewModel.getCurrentWord();
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
     * @param  view The clicked button
     */
    public void checkWord(@SuppressWarnings("unused") View view) {
        if (!mViewModel.hasNextEntry()) {
            //TODO: show statistics or similar instead of just returning.
            finish();
        }


        String typedTranslation = mBinding.etTranslatedWord.getText().toString().trim()
                .toLowerCase();

        if (typedTranslation.isEmpty()) {
            Snackbar.make(mBinding.activityTraining, R.string
                    .activity_training_empty_translation, Snackbar.LENGTH_SHORT).show();
            return;
        }

        LibraryEntry currentEntry = mViewModel.getCurrentWord();
        String correctTranslation;
        if (!reverseTraining) {
            correctTranslation = currentEntry.getForeignWord().trim().toLowerCase();
        } else
            correctTranslation = currentEntry.getNativeWord().trim().toLowerCase();


        double updatedKnowledgeLevel;
        boolean trainingCorrect = typedTranslation.equals(correctTranslation);

        if (trainingCorrect) {
            Snackbar correct = Snackbar.make(mBinding.activityTraining, R.string.correct, Snackbar
                    .LENGTH_SHORT);
            correct.getView().setBackgroundColor(KnowledgeLevelUtils.getColorByKnowledgeLevel
                    (this, 5));
            correct.show();
            updatedKnowledgeLevel = KnowledgeLevelUtils.calculateKnowledgeLevelAdjustment(currentEntry
                    .getKnowledgeLevel(), true);
            mCulaRepository.insertStatisticsEntry(new StatisticEntry(currentEntry.getId(),
                    null, 1));
        } else {
            Snackbar wrong = Snackbar.make(mBinding.activityTraining, R.string.wrong, Snackbar
                    .LENGTH_SHORT);
            wrong.getView().setBackgroundColor(KnowledgeLevelUtils.getColorByKnowledgeLevel
                    (this, 0));
            wrong.show();
            updatedKnowledgeLevel = KnowledgeLevelUtils.calculateKnowledgeLevelAdjustment(currentEntry
                    .getKnowledgeLevel(), false);
            mCulaRepository.insertStatisticsEntry(new StatisticEntry(currentEntry.getId(),
                    null, 0));
        }
        currentEntry.setKnowledgeLevel(updatedKnowledgeLevel);
        mCulaRepository.updateLibraryEntry(currentEntry);
        showWord(true);
    }
}
