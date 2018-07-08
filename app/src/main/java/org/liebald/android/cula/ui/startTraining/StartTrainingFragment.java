package org.liebald.android.cula.ui.startTraining;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.liebald.android.cula.R;
import org.liebald.android.cula.data.database.Entities.LessonEntry;
import org.liebald.android.cula.databinding.FragmentStartTrainingBinding;
import org.liebald.android.cula.utilities.InjectorUtils;

import java.util.Objects;

/**
 * The fragment for starting a training session.
 */
public class StartTrainingFragment extends Fragment {


    /**
     * {@link Bundle} key for the amount of words to be trained.
     */
    public static final String BUNDLE_EXTRA_NUMWORDS_KEY = "NumberOfWords";
    /**
     * {@link Bundle} key for the Lesson to be trained.
     */
    public static final String BUNDLE_EXTRA_LESSON_KEY = "Lesson";
    /**
     * {@link Bundle} key for the KnowledgeLevel of words to be trained.
     */
    public static final String BUNDLE_EXTRA_KNOWLEDGE_LEVEL_KEY = "KnowledgeLevel";
    /**
     * {@link Bundle} key for the KnowledgeLevel range of words to be trained
     * (exactly the knowledge level or also higher/lower).
     */
    public static final String BUNDLE_EXTRA_KNOWLEDGE_LEVEL_RANGE_KEY = "KnowledgeLevelRange";
    /**
     * Tag for logging.
     */
    private static final String TAG = StartTrainingFragment.class.getSimpleName();


    /**
     * The Databinding for the layout.
     */
    private FragmentStartTrainingBinding mBinding;

    /**
     * The viewModel for the {@link StartTrainingFragment}.
     */
    private StartTrainingViewModel mViewModel;


    public StartTrainingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() == null || getContext() == null)
            return;
        // Create the ViewModel for this fragment.
        StartTrainingViewModelFactory factory =
                InjectorUtils.provideStartTrainingViewModelFactory(getContext());
        mViewModel = ViewModelProviders.of(getActivity(), factory).get(StartTrainingViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_start_training,
                container, false);

        // set listener for start training button
        mBinding.btStartTraining.setOnClickListener(this::startTraining);

        //TODO: set initially selected options based on last session (sharedParameters).

        // Set the correct Entries for the number of words to learn spinner.
        ArrayAdapter<CharSequence> num_words_adapter =
                ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                        R.array.training_number_of_words, android.R.layout.simple_spinner_item);
        num_words_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spStartTrainingNumberOfWords.setAdapter(num_words_adapter);

        // Set the correct Entries for the Knowledge Level Range Spinner.
        ArrayAdapter<CharSequence> knowledgeLevel_range_adapter =
                ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                        R.array.knowledgeLevel_range, android.R.layout.simple_spinner_item);
        knowledgeLevel_range_adapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        mBinding.spStartTrainingKnowledgeLevelRange.setAdapter(knowledgeLevel_range_adapter);

        // Set the correct Entries for the Knowledge Level Spinner.
        ArrayAdapter<CharSequence> knowledgeLevel_adapter =
                ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                        R.array.knowledgeLevel, android.R.layout.simple_spinner_item);
        knowledgeLevel_adapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        mBinding.spStartTrainingKnowledgeLevel.setAdapter(knowledgeLevel_adapter);

        // Set the correct Entries for the Lessons taken from the database.
        mViewModel.getLessonEntries().observe(this, entries -> {
            if (entries != null) {
                mViewModel.getLessonEntries().removeObservers(this);
                String[] lessonNames = new String[entries.size() + 1];
                lessonNames[0] = getResources().getString(R.string.start_training_lessons_any);
                int index = 1;
                for (LessonEntry entry : entries) {
                    lessonNames[index] = entry.getLessonName();
                    index++;
                }
                ArrayAdapter<CharSequence> lesson_adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item, lessonNames);
                lesson_adapter.setDropDownViewResource(android.R.layout
                        .simple_spinner_dropdown_item);
                mBinding.spStartTrainingLesson.setAdapter(lesson_adapter);
            }
        });

        return mBinding.getRoot();
    }

    /**
     * Start the training Activity with correct settings.
     *
     * @param view The button that called this method.
     */
    public void startTraining(View view) {
        Toast.makeText(getContext(), "Starting Training", Toast.LENGTH_SHORT).show();


        //TODO: save selected options to sharedParameters for next session
        //TODO: pass selected parameters as options to training activity
        //TODO: start training activity
    }

}
