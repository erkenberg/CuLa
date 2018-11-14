package com.sliebald.cula.ui.training;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.sliebald.cula.Analytics;
import com.sliebald.cula.R;
import com.sliebald.cula.data.CulaRepository;
import com.sliebald.cula.data.database.Entities.LibraryEntry;
import com.sliebald.cula.data.database.Entities.StatisticEntry;
import com.sliebald.cula.databinding.ActivityTrainingBinding;
import com.sliebald.cula.ui.startTraining.StartTrainingFragment;
import com.sliebald.cula.utilities.InjectorUtils;
import com.sliebald.cula.utilities.KnowledgeLevelUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

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

        Analytics.logEventStartTraining(this);

        // get DB access
        mCulaRepository = InjectorUtils.provideRepository();
        Log.d(TAG, "SAVED STATE: " + savedInstanceState);
        //set back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.drawer_label_train);
            getSupportActionBar().setSubtitle(PreferenceManager.getDefaultSharedPreferences(this)
                    .getString(getString(R.string.settings_select_language_key), ""));
        }

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
            Analytics.logEventStopTraining(this);

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
            // Print the current progress
            mBinding.trainingProgress.tvLabelProgress.setText(getString(R.string
                            .activity_training_label_progress,
                    mViewModel.getLearningSetPosition(), mViewModel.getLearningSetSize()));
            // Update the progressbar
            int progress = ((mViewModel.getLearningSetPosition() - 1) * 100) / mViewModel
                    .getLearningSetSize();
            mBinding.trainingProgress.pgProgress.setProgress(progress);
        }
    }

    /**
     * Check the current word and move to the next.
     *
     * @param  view The clicked button
     */
    public void checkWord(@SuppressWarnings("unused") View view) {
        //todo: refactor snackbar creation, lots of boilerplate code.
        // If there is no next word to train, return
        if (!mViewModel.hasNextEntry()) {
            //TODO: show statistics or similar instead of just returning.
            Toast.makeText(this, R.string.activity_training_training_finished, Toast.LENGTH_SHORT)
                    .show();
            finish();
        }


        String typedTranslation = mBinding.etTranslatedWord.getText().toString().trim()
                .toLowerCase();
        // get the correct translation
        LibraryEntry currentEntry = mViewModel.getCurrentWord();
        String correctTranslation;
        if (!reverseTraining) {
            correctTranslation = currentEntry.getForeignWord().trim().toLowerCase();
        } else
            correctTranslation = currentEntry.getNativeWord().trim().toLowerCase();

        // check whether actually something entered and ask if they want to skip the word if not.
        if (typedTranslation.isEmpty()) {

            Snackbar skipBar = Snackbar.make(mBinding.activityTraining, R.string
                    .activity_training_skip_word, Snackbar.LENGTH_LONG);

            skipBar.setAction(R.string.skip, view1 -> {
                SpannableStringBuilder snackbarText = new SpannableStringBuilder();
                snackbarText.append(getString(R.string
                        .activity_training_skip_translation, mBinding.tvLabelWordToTranslate
                        .getText
                                ().toString()));
                int correctStart = snackbarText.length();
                snackbarText.append(correctTranslation);
                snackbarText.setSpan(new ForegroundColorSpan(ContextCompat.getColor
                        (getApplicationContext(), R.color
                                .colorPrimary)), correctStart, snackbarText.length(), Spannable
                        .SPAN_EXCLUSIVE_EXCLUSIVE);
                snackbarText.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), correctStart,
                        snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                snackbarText.setSpan(new RelativeSizeSpan(1.2f), correctStart, snackbarText
                                .length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                Snackbar.make(mBinding.activityTraining, snackbarText, Snackbar
                        .LENGTH_LONG).show();
                showWord(true);
            });
            skipBar.show();


            return;
        }



        //check whether the trained word was correct and act accordingly
        double updatedKnowledgeLevel;
        boolean trainingCorrect = typedTranslation.equals(correctTranslation);

        if (trainingCorrect) {
            SpannableStringBuilder snackbarText = new SpannableStringBuilder();
            snackbarText.append(getString(R.string.correct));
            snackbarText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color
                    .green)), 0, snackbarText.length(), Spannable
                    .SPAN_EXCLUSIVE_EXCLUSIVE);
            snackbarText.setSpan(new StyleSpan(Typeface.BOLD), 0,
                    snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Snackbar correct = Snackbar.make(mBinding.activityTraining, snackbarText, Snackbar
                    .LENGTH_SHORT);
            correct.show();
            updatedKnowledgeLevel = KnowledgeLevelUtils.calculateKnowledgeLevelAdjustment(currentEntry
                    .getKnowledgeLevel(), true);
            mCulaRepository.insertStatisticsEntry(new StatisticEntry(currentEntry.getId(),
                    null, 1));
        } else {
            // create and format the snackbar feedback
            SpannableStringBuilder snackbarText = new SpannableStringBuilder();
            snackbarText.append(getString(R.string.wrong));
            snackbarText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color
                    .red)), 0, snackbarText.length(), Spannable
                    .SPAN_EXCLUSIVE_EXCLUSIVE);
            snackbarText.setSpan(new StyleSpan(Typeface.BOLD), 0,
                    snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            snackbarText.append(getString(R.string
                    .activity_training_wrong_translation, mBinding.tvLabelWordToTranslate.getText
                    ().toString()));
            int correctStart = snackbarText.length();
            snackbarText.append(correctTranslation);
            snackbarText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color
                    .colorPrimary)), correctStart, snackbarText.length(), Spannable
                    .SPAN_EXCLUSIVE_EXCLUSIVE);
            snackbarText.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), correctStart,
                    snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            snackbarText.setSpan(new RelativeSizeSpan(1.2f), correctStart, snackbarText.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Snackbar wrong = Snackbar.make(mBinding.activityTraining, snackbarText, Snackbar
                    .LENGTH_LONG);
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
