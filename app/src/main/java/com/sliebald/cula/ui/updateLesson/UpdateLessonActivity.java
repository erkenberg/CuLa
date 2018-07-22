package com.sliebald.cula.ui.updateLesson;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;

import com.sliebald.cula.R;
import com.sliebald.cula.data.CulaRepository;
import com.sliebald.cula.data.database.Entities.LessonEntry;
import com.sliebald.cula.data.database.Entities.LessonMappingEntry;
import com.sliebald.cula.data.database.Pojos.MappingPOJO;
import com.sliebald.cula.databinding.ActivityUpdateLessonBinding;
import com.sliebald.cula.utilities.InjectorUtils;

/**
 * Activity that manages editing and adding lessons.
 */
public class UpdateLessonActivity extends AppCompatActivity implements
        UpdateLessonRecyclerViewAdapter.OnItemClickListener, CulaRepository
        .OnLessonEntryAddedListener {

    /**
     * Tag of this activity.
     */
    private static final String TAG = UpdateLessonActivity.class.getSimpleName();


    /**
     * When the intend passed to this activity contains a bundle with this extra
     * an existing {@link LessonEntry} should be updated instead of adding a new one.
     * The id of the entry is passed as the value of the key.
     */
    public static final String BUNDLE_EXTRA_UPDATE_KEY = "LessonEntryId";

    /**
     * The databinding for the Layout.
     */
    private ActivityUpdateLessonBinding mBinding;

    /**
     * The {@link CulaRepository} that provides access to all data sources.
     */
    private CulaRepository mCulaRepository;

    /**
     * Adapter for the list of words that could belong to the lesson.
     */
    private UpdateLessonRecyclerViewAdapter mAdapter;


    private UpdateLessonViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //load databinding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_update_lesson);

        //get the Repository for db access
        mCulaRepository = InjectorUtils.provideRepository();

        //enable the back button
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);

        //Make sure the keyboard doesn't show on activity start.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //create and set adapter for the mappings
        mAdapter = new UpdateLessonRecyclerViewAdapter(this);
        mBinding.recyclerViewLessonMappingList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        mBinding.recyclerViewLessonMappingList.setAdapter(mAdapter);

        //Get the intent that startet the activity for special treatments
        Intent intent = getIntent();
        int id = -1;
        // If an existing entry should be updated the id of the entry is given:
        if (intent.hasExtra(BUNDLE_EXTRA_UPDATE_KEY)) {
            id = intent.getIntExtra(BUNDLE_EXTRA_UPDATE_KEY, -1);
        }
        //create the view model
        UpdateLessonViewModelFactory viewModelFactory = new UpdateLessonViewModelFactory(id);
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(UpdateLessonViewModel.class);
        if (savedInstanceState == null)
            mViewModel.setEntryID(id);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Only when editing an existing lesson the UI should be filled
        if (mViewModel.getEntryID() != -1) {
            updateUI();
        } else {
            mBinding.tvLabelAddLessonMappings.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Retrieves the mapping for the currently updated Lesson.
     */
    private void updateUI() {
        mViewModel.getEntry().observe(this, lessonEntry -> {
            if (lessonEntry == null)
                return;
            mViewModel.getEntry().removeObservers(this);
            mBinding.etAddLessonName.setText(lessonEntry.getLessonName());
            mBinding.etAddLessonDescription.setText(lessonEntry.getLessonDescription());
        });
        mViewModel.getMapping().observe(this, mappingPOJOList -> {
            if (mappingPOJOList == null)
                return;
            mViewModel.getEntry().removeObservers(this);
            mAdapter.swapEntries(mappingPOJOList);
            Log.d(UpdateLessonActivity.class.getSimpleName(), "Elements in mapping list: " +
                    mappingPOJOList.size());
            for (MappingPOJO mappingPOJO : mappingPOJOList)
                Log.d(UpdateLessonActivity.class.getSimpleName(), mappingPOJO.toString());
        });
        mBinding.tvLabelAddLessonMappings.setVisibility(View.VISIBLE);
    }


    /**
     * Callback for the add/update lesson button.
     * Checks whether the lesson name is not empty and triggers the lesson update.
     *
     * @param view The view that was clicked.
     */
    public void commitLessonEntry(View view) {
        String lessonName = mBinding.etAddLessonName.getText().toString().trim();
        String lessonDescription = mBinding.etAddLessonDescription.getText().toString().trim();
        if (lessonName.isEmpty()) {
            Snackbar.make(mBinding.activityUpdateLesson, R.string
                    .update_lesson_toast_lesson_name_empty, Snackbar.LENGTH_SHORT)
                    .show();
            mBinding.etAddLessonName.requestFocus();
            return;
        }

        String language = PreferenceManager.getDefaultSharedPreferences(this).getString(getString
                (R.string.settings_select_language_key), "");
        if (mViewModel.getEntryID() != -1) {
            Log.d(TAG, "Updating existing lesson: " + mViewModel.getEntryID());
            mCulaRepository.updateLessonEntry(new LessonEntry(mViewModel.getEntryID(), lessonName,
                    lessonDescription, language));
            Snackbar.make(mBinding.activityUpdateLesson, R.string
                    .update_lesson_toast_lesson_updated, Snackbar.LENGTH_SHORT).show();

        } else {
            Log.d(TAG, "Inserting new lesson");
            mCulaRepository.insertLessonEntry(this, new LessonEntry(lessonName,
                    lessonDescription, language));
            Snackbar.make(mBinding.activityUpdateLesson, R.string
                    .update_lesson_toast_lesson_added, Snackbar.LENGTH_SHORT).show();

        }

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Callback for the return button.
     *
     */
    public void returnWithoutSave(@SuppressWarnings("unused") View view) {
        finish();
    }


    @Override
    public void onLessonEntryClick(CheckBox check, int id) {
        //TODO: keep scroll position on update.
        if (mViewModel.getEntry().getValue() == null)
            return;
        if (check.isChecked()) {
            mCulaRepository.insertLessonMappingEntry(new LessonMappingEntry(mViewModel.getEntry()
                    .getValue().getId(), id));
        } else {
            mCulaRepository.deleteLessonMappingEntry(new LessonMappingEntry(mViewModel.getEntry()
                    .getValue().getId(), id));

        }
    }

    @Override
    public void onLessonEntryAdded(long[] ids) {
        //Currently we only add one lesson at a time, therefore we just check the first element.
        if (ids.length > 0) {
            mViewModel.setEntryID((int) ids[0]);
//            mViewModel.updateViewModel(mViewModel.getEntryID());
            Log.d(TAG, "entryID: " + ids[0]);
            runOnUiThread(this::updateUI);
        }
    }

}
