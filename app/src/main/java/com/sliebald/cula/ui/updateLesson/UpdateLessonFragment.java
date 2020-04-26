package com.sliebald.cula.ui.updateLesson;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.google.android.material.snackbar.Snackbar;
import com.sliebald.cula.R;
import com.sliebald.cula.databinding.FragmentUpdateLessonBinding;
import com.sliebald.cula.utilities.KeyboardUtils;
import com.sliebald.cula.utilities.SortUtils;

/**
 * Fragment that manages editing and adding lessons.
 */
public class UpdateLessonFragment extends Fragment implements
        UpdateLessonRecyclerViewAdapter.OnItemClickListener, SortUtils.OnSortChangedListener {

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
        mBinding.recyclerViewLessonMappingList.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        mBinding.recyclerViewLessonMappingList.setAdapter(mAdapter);
        UpdateLessonFragmentArgs args = UpdateLessonFragmentArgs.fromBundle(requireArguments());

        int lessonId = args.getLessonId();

        //create the view model
        UpdateLessonViewModelFactory viewModelFactory = new UpdateLessonViewModelFactory(lessonId);
        mViewModel = new ViewModelProvider(this, viewModelFactory).get(UpdateLessonViewModel.class);

        //set button callbacks
        mBinding.buttonAddLesson.setOnClickListener(v -> commitLessonEntry());
        mBinding.buttonReturn.setOnClickListener(v -> finishLessonUpdate());

        mViewModel.getEntry().observe(getViewLifecycleOwner(), lessonEntry -> {
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
        mViewModel.getMapping().observe(getViewLifecycleOwner(), mappingPOJOList -> {
            if (mappingPOJOList == null) {
                return;
            }
            mAdapter.swapEntries(mappingPOJOList);
        });
        setHasOptionsMenu(true);
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
            Snackbar.make(mBinding.activityUpdateLesson, R.string.update_lesson_toast_lesson_name_empty, Snackbar.LENGTH_SHORT).show();
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
        KeyboardUtils.hideKeyboard(requireContext(), getView());

    }

    /**
     * Return to the lesson overview fragment
     */
    private void finishLessonUpdate() {
        KeyboardUtils.hideKeyboard(requireContext(), getView());
        Navigation.findNavController(requireView()).popBackStack();
    }

    @Override
    public void onLessonEntryClick(CheckBox check, int id) {
        mViewModel.insertOrDeleteLessonMappingEntry(check.isChecked(), id);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.sort_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_sort) {// Open a sort dialog and set this fragment as target for the callback.
            UpdateLessonSortDialog updateLessonSortDialog = new UpdateLessonSortDialog();
            Bundle args = new Bundle();
            args.putString(SortUtils.KEY_ACTIVE_SORT_BY, mViewModel.getCurrentSortType().name());
            args.putBoolean(SortUtils.KEY_ACTIVE_SORT_ORDER, mViewModel.getCurrentSortOrder());
            updateLessonSortDialog.setArguments(args);
            updateLessonSortDialog.setTargetFragment(this, 1);
            updateLessonSortDialog.show(getParentFragmentManager(), "SortDialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUpdateSortOrderClick(SortUtils.SortType type, boolean asc) {
        mViewModel.sortMappingBy(type, asc);
    }
}
