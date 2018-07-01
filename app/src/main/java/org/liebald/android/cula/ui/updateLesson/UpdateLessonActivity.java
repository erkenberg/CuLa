package org.liebald.android.cula.ui.updateLesson;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.liebald.android.cula.R;
import org.liebald.android.cula.data.CulaRepository;
import org.liebald.android.cula.data.database.Entities.LessonEntry;
import org.liebald.android.cula.data.database.Entities.MappingPOJO;
import org.liebald.android.cula.databinding.ActivityUpdateLessonBinding;
import org.liebald.android.cula.utilities.InjectorUtils;

public class UpdateLessonActivity extends AppCompatActivity {


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
     * The entryId of an entry that is updated. Only used when !=-1.
     */
    private int entryId = -1;

    /**
     * The shared preferences for accessing default values.
     */
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_update_lesson);
        mCulaRepository = InjectorUtils.provideRepository(this);
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        int id;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // If an existing entry should be updated:
        if (intent.hasExtra(BUNDLE_EXTRA_UPDATE_KEY) && (id = intent.getIntExtra(BUNDLE_EXTRA_UPDATE_KEY, -1)) != -1) {
            mBinding.buttonAddLesson.setVisibility(View.GONE);


            UpdateLessonViewModelFactory viewModelFactory = new UpdateLessonViewModelFactory(mCulaRepository, id);
            final UpdateLessonViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(UpdateLessonViewModel.class);
            viewModel.getEntry().observe(this, lessonEntry -> {
                if (lessonEntry == null)
                    return;
                viewModel.getEntry().removeObservers(this);
                mBinding.etAddLessonName.setText(lessonEntry.getLessonName());
                mBinding.etAddLessonDescription.setText(lessonEntry.getLessonDescription());

                entryId = lessonEntry.getId();
            });

            viewModel.getMapping().observe(this, mappingPOJOList -> {
                if (mappingPOJOList == null)
                    return;
                viewModel.getEntry().removeObservers(this);
                Log.d(UpdateLessonActivity.class.getSimpleName(), "Elements in mapping list: " + mappingPOJOList.size());
                for (MappingPOJO pojo : mappingPOJOList)
                    Log.d(UpdateLessonActivity.class.getSimpleName(), pojo.toString());

            });

        } else {
            //TODO: anything to do if adding a new ViewModel?
        }


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
        //TODO: replacable by snackbar?
        if (lessonName.isEmpty()) {
            Toast.makeText(this, "Lesson name can't be empty", Toast.LENGTH_LONG).show();
            mBinding.etAddLessonName.requestFocus();
            return;
        }

        String language = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.settings_select_language_key), "");
        if (entryId != -1) {
            //TODO: replace with update method?
            mCulaRepository.insertLessonEntry(new LessonEntry(entryId, lessonName, lessonDescription, language));
        } else {
            mCulaRepository.insertLessonEntry(new LessonEntry(lessonName, lessonDescription, language));
        }

        if (view.getId() == R.id.button_add_lesson_return)
            finish();
        else {
            // mBinding.editTextAddNativeWord.setText("");
            // mBinding.editTextAddForeignWord.setText("");
            //mBinding.editTextAddNativeWord.requestFocus();
            //TODO: replace by snackbar?
            Toast.makeText(this, "Added lesson", Toast.LENGTH_LONG).show();
        }
    }

}
