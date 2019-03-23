package com.sliebald.cula.ui.updateLesson;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.google.android.material.snackbar.Snackbar;
import com.sliebald.cula.R;
import com.sliebald.cula.data.database.Pojos.MappingPOJO;
import com.sliebald.cula.databinding.FragmentUpdateLessonBinding;
import com.sliebald.cula.utilities.KeyboardUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;

/**
 * Activity that manages editing and adding lessons.
 */
public class UpdateLessonFragment extends Fragment implements
        UpdateLessonRecyclerViewAdapter.OnItemClickListener {

    /**
     * Tag of this activity.
     */
    private static final String TAG = UpdateLessonFragment.class.getSimpleName();

    /**
     * The data binding for the Layout.
     */
    private FragmentUpdateLessonBinding mBinding;

    /**
     * Adapter for the list of words that could belong to the lesson.
     */
    private UpdateLessonRecyclerViewAdapter mAdapter;


    private UpdateLessonViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_update_lesson, container, false);

        mAdapter = new UpdateLessonRecyclerViewAdapter(this);
        mBinding.recyclerViewLessonMappingList.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        mBinding.recyclerViewLessonMappingList.setAdapter(mAdapter);
        UpdateLessonFragmentArgs args = UpdateLessonFragmentArgs.fromBundle(getArguments());

        int lessonId = args.getLessonId();

        //create the view model
        UpdateLessonViewModelFactory viewModelFactory = new UpdateLessonViewModelFactory(lessonId);
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(UpdateLessonViewModel.class);

        //set button callbacks
        mBinding.buttonAddLesson.setOnClickListener(v -> commitLessonEntry());
        mBinding.buttonReturn.setOnClickListener(v -> finishLessonUpdate());


        mViewModel.getEntry().observe(this, lessonEntry -> {
            Log.d(TAG, "test" + lessonEntry);
            if (lessonEntry == null || lessonEntry.getId() < 0) {
                mBinding.tvLabelAddLessonMappings.setVisibility(View.INVISIBLE);
                mBinding.recyclerViewLessonMappingList.setVisibility(View.GONE);
                mBinding.buttonAddLesson.setText(R.string.add);
                return;
            }
            mBinding.tvLabelAddLessonMappings.setVisibility(View.VISIBLE);
            mBinding.recyclerViewLessonMappingList.setVisibility(View.VISIBLE);
            mBinding.buttonAddLesson.setText(R.string.update_library_button_save_word);

            mBinding.etAddLessonName.setText(lessonEntry.getLessonName());
            mBinding.etAddLessonDescription.setText(lessonEntry.getLessonDescription());
        });
        mViewModel.getMapping().observe(this, mappingPOJOList -> {
            if (mappingPOJOList == null) {
                return;
            }
            mAdapter.swapEntries(mappingPOJOList);
            Log.d(UpdateLessonFragment.class.getSimpleName(), "Elements in mapping list: " +
                    mappingPOJOList.size());
            for (MappingPOJO mappingPOJO : mappingPOJOList)
                Log.d(UpdateLessonFragment.class.getSimpleName(), mappingPOJO.toString());
        });

        return mBinding.getRoot();
    }


    /**
     * Callback for the add/update lesson button.
     * Checks whether the lesson name is not empty and triggers the lesson update.
     */
    private void commitLessonEntry() {
        String lessonName = mBinding.etAddLessonName.getText().toString().trim();
        String lessonDescription = mBinding.etAddLessonDescription.getText().toString().trim();
        if (lessonName.isEmpty()) {
            Snackbar.make(mBinding.activityUpdateLesson, R.string
                    .update_lesson_toast_lesson_name_empty, Snackbar.LENGTH_SHORT)
                    .show();
            mBinding.etAddLessonName.requestFocus();
            return;
        }

        if (mViewModel.addOrUpdateLesson(lessonName, lessonDescription)) {
            Snackbar.make(mBinding.activityUpdateLesson, R.string
                    .update_lesson_toast_lesson_added, Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(mBinding.activityUpdateLesson, R.string
                    .update_lesson_toast_lesson_updated, Snackbar.LENGTH_SHORT).show();
        }
        KeyboardUtils.hideKeyboard(getContext(), getView());

    }


    /**
     * Finishes the training session.
     */
    private void finishLessonUpdate() {
        KeyboardUtils.hideKeyboard(getContext(), getView());
        Navigation.findNavController(getView()).navigate(R.id.action_updateLessonFragment_to_lessons_dest);
    }


    @Override
    public void onLessonEntryClick(CheckBox check, int id) {
        //TODO: keep scroll position on update.
        mViewModel.insertOrDeleteLessonMappingEntry(check.isChecked(), id);

    }


}
