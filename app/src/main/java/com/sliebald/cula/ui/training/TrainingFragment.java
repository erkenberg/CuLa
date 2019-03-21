package com.sliebald.cula.ui.training;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.snackbar.Snackbar;
import com.sliebald.cula.R;
import com.sliebald.cula.data.CulaRepository;
import com.sliebald.cula.data.database.Pojos.TrainingData;
import com.sliebald.cula.databinding.FragmentTrainingBinding;
import com.sliebald.cula.utilities.InjectorUtils;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;


public class TrainingFragment extends Fragment {


    /**
     * Tag of this activity.
     */
    private static final String TAG = TrainingFragment.class.getSimpleName();


    /**
     * The data binding for the Layout.
     */
    private FragmentTrainingBinding mBinding;

    /**
     * The {@link CulaRepository} that provides access to all data sources.
     */
    private CulaRepository mCulaRepository;

    /**
     * ViewModel of the activity.
     */
    private TrainingViewModel mViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // create DataBinding
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_training, container, false);
        mBinding.btCheck.setOnClickListener(v -> checkWord());
        // get DB access
        mCulaRepository = InjectorUtils.provideRepository();
        // get the fragmentArgs that started the learning activity and extract the relevant values.
        //TODO: dummy fragmentArgs
        TrainingFragmentArgs fragmentArgs = TrainingFragmentArgs.fromBundle(getArguments());
        TrainingData data = fragmentArgs.getTrainingData();
        //create the view model
        TrainingViewModelFactory viewModelFactory = new TrainingViewModelFactory(data);
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(TrainingViewModel.class);
        showWord();
        return mBinding.getRoot();
    }


    /**
     * Display the next word to learn.
     */
    private void showWord() {
        if (mViewModel.isTrainingOver()) {
            mBinding.btCheck.setText(getString(R.string.finish_training));
            mBinding.etTranslatedWord.setEnabled(false);
            mBinding.trainingProgress.tvLabelProgress.setText(getString(R.string
                            .activity_training_label_progress,
                    mViewModel.getLearningSetPosition(), mViewModel.getLearningSetSize()));
            mBinding.trainingProgress.pgProgress.setProgress(100);
            mBinding.btCheck.setOnClickListener(v -> finishTraining());
            return;
        }
        mBinding.etTranslatedWord.getText().clear();
        mBinding.etTranslatedWord.requestFocus();
        mBinding.tvLabelWordToTranslate.setText(mViewModel.getCurrentWord());

        // Print the current progress
        mBinding.trainingProgress.tvLabelProgress.setText(getString(R.string
                        .activity_training_label_progress,
                mViewModel.getLearningSetPosition() - 1, mViewModel.getLearningSetSize()));
        // Update the progressbar
        int progress = ((mViewModel.getLearningSetPosition() - 1) * 100) / mViewModel
                .getLearningSetSize();
        mBinding.trainingProgress.pgProgress.setProgress(progress);

    }

    /**
     * Finishes the training session.
     */
    private void finishTraining() {
        //TODO: show statistics or similar instead of just returning.

        InputMethodManager imm =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getRootView().getWindowToken(), 0);
        Navigation.findNavController(getView()).navigate(R.id.action_training_dest_to_startTraining_dest);
    }

    /**
     * Check the current word and move to the next.
     */
    private void checkWord() {
        //todo: refactor snackbar creation, lots of boilerplate code.
        String typedTranslation = mBinding.etTranslatedWord.getText().toString().trim();
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
                snackbarText.append(mViewModel.getCorrectTranslation());
                snackbarText.setSpan(new ForegroundColorSpan(ContextCompat.getColor
                        (getContext(), R.color
                                .colorPrimary)), correctStart, snackbarText.length(), Spannable
                        .SPAN_EXCLUSIVE_EXCLUSIVE);
                snackbarText.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), correctStart,
                        snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                snackbarText.setSpan(new RelativeSizeSpan(1.2f), correctStart, snackbarText
                                .length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                Snackbar.make(mBinding.activityTraining, snackbarText, Snackbar
                        .LENGTH_LONG).show();
                mViewModel.next();
                showWord();
            });
            skipBar.show();
            return;
        } else if (mViewModel.checkTranslationCorrect(typedTranslation)) {
            SpannableStringBuilder snackbarText = new SpannableStringBuilder();
            snackbarText.append(getString(R.string.correct));
            snackbarText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color
                    .green)), 0, snackbarText.length(), Spannable
                    .SPAN_EXCLUSIVE_EXCLUSIVE);
            snackbarText.setSpan(new StyleSpan(Typeface.BOLD), 0,
                    snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Snackbar correct = Snackbar.make(mBinding.activityTraining, snackbarText, Snackbar
                    .LENGTH_SHORT);
            correct.show();
        } else {
            // create and format the snackbar feedback
            SpannableStringBuilder snackbarText = new SpannableStringBuilder();
            snackbarText.append(getString(R.string.wrong));
            snackbarText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color
                    .red)), 0, snackbarText.length(), Spannable
                    .SPAN_EXCLUSIVE_EXCLUSIVE);
            snackbarText.setSpan(new StyleSpan(Typeface.BOLD), 0,
                    snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            snackbarText.append(getString(R.string
                    .activity_training_wrong_translation, mBinding.tvLabelWordToTranslate.getText
                    ().toString()));
            int correctStart = snackbarText.length();
            snackbarText.append(mViewModel.getCorrectTranslation());
            snackbarText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color
                    .colorPrimary)), correctStart, snackbarText.length(), Spannable
                    .SPAN_EXCLUSIVE_EXCLUSIVE);
            snackbarText.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), correctStart,
                    snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            snackbarText.setSpan(new RelativeSizeSpan(1.2f), correctStart, snackbarText.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Snackbar wrong = Snackbar.make(mBinding.activityTraining, snackbarText, Snackbar
                    .LENGTH_LONG);
            wrong.show();

        }
        mViewModel.next();
        showWord();
    }

}
