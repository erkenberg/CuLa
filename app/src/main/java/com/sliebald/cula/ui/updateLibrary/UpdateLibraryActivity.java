package com.sliebald.cula.ui.updateLibrary;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sliebald.cula.R;
import com.sliebald.cula.data.CulaRepository;
import com.sliebald.cula.data.database.Entities.LibraryEntry;
import com.sliebald.cula.databinding.ActivityUpdateLibraryBinding;
import com.sliebald.cula.utilities.InjectorUtils;

import java.util.Date;

public class UpdateLibraryActivity extends AppCompatActivity {


    /**
     * When the intend passed to this activity contains a bundle with this extra
     * an existing {@link LibraryEntry}
     * should be updated instead of adding a new one.
     * The id of the entry is passed as the value of the key.
     */
    public static final String BUNDLE_EXTRA_UPDATE_KEY = "LibraryEntryId";

    /**
     * The data binding for the Layout.
     */
    private ActivityUpdateLibraryBinding mBinding;

    /**
     * The {@link CulaRepository} that provides access to all data sources.
     */
    private CulaRepository mCulaRepository;

    /**
     * The entryId of an entry that is updated. Only used when !=-1.
     */
    private int entryId = -1;

    /**
     * The currently selected knowledge day.
     */
    private double selectedKnowledgeLevel;

    /**
     * The shared preferences for accessing default values.
     */
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create Data binding
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_update_library);

        // get DB access
        mCulaRepository = InjectorUtils.provideRepository();

        //set back button
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        // get the shared preferences
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // if an ID is given as extra an existing entry should be loaded and updated instead of
        // creating a new one.
        int id;
        if (intent.hasExtra(BUNDLE_EXTRA_UPDATE_KEY) && (id = intent.getIntExtra
                (BUNDLE_EXTRA_UPDATE_KEY, -1)) != -1) {
            mBinding.buttonAddWordPair.setVisibility(View.GONE);
            UpdateLibraryViewModelFactory viewModelFactory = new UpdateLibraryViewModelFactory
                    (id);
            final UpdateLibraryViewModel viewModel = ViewModelProviders.of(this,
                    viewModelFactory).get(UpdateLibraryViewModel.class);
            viewModel.getEntry().observe(this, libraryEntry -> {
                if (libraryEntry == null)
                    return;
                viewModel.getEntry().removeObservers(this);
                mBinding.editTextAddForeignWord.setText(libraryEntry.getForeignWord());
                mBinding.editTextAddNativeWord.setText(libraryEntry.getNativeWord());

                setKnowledgeLevelUI(libraryEntry.getKnowledgeLevel());

                entryId = libraryEntry.getId();
            });

        } else {
            //on creating a new entry load the default day set in the settings.
            setKnowledgeLevelUI(Double.parseDouble(mSharedPreferences.getString(getString(R.string
                    .settings_default_knowledgeLevel_key), getString
                    (R.string.settings_default_knowledgeLevel_default))));
        }

        //add an onCheckedChange listener to set the internal current knowledge day correctly.
        mBinding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_knowledgeLevel_1:
                    selectedKnowledgeLevel = 0.5;
                    break;
                case R.id.rb_knowledgeLevel_2:
                    selectedKnowledgeLevel = 1.5;
                    break;
                case R.id.rb_knowledgeLevel_3:
                    selectedKnowledgeLevel = 2.5;
                    break;
                case R.id.rb_knowledgeLevel_4:
                    selectedKnowledgeLevel = 3.5;
                    break;
                case R.id.rb_knowledgeLevel_5:
                    selectedKnowledgeLevel = 4.5;
                    break;
            }
        });

    }

    /**
     * Callback for the add/update word button.
     * Checks whether the words are not empty and triggers the library update.
     *
     * @param view The view that was clicked.
     */
    public void commitLibraryEntry(View view) {
        String nativeWord = mBinding.editTextAddNativeWord.getText().toString().trim();
        String foreignWord = mBinding.editTextAddForeignWord.getText().toString().trim();
        if (nativeWord.isEmpty()) {
            Snackbar.make(mBinding.activityUpdateLibrary, R.string
                    .update_library_warning_native_word_missing, Snackbar.LENGTH_SHORT).show();

            mBinding.editTextAddNativeWord.requestFocus();
            return;
        }
        if (foreignWord.isEmpty()) {
            Snackbar.make(mBinding.activityUpdateLibrary, R.string
                    .update_library_warning_foreign_word_missing, Snackbar.LENGTH_SHORT).show();

            mBinding.editTextAddForeignWord.requestFocus();
            return;
        }
        String language = PreferenceManager.getDefaultSharedPreferences(this).getString
                (getString(R.string.settings_select_language_key), "");
        if (entryId != -1) {
            //TODO: day is changed, even if not actually changed in the UI.
            mCulaRepository.updateLibraryEntry(new LibraryEntry(entryId, nativeWord, foreignWord,
                    language, selectedKnowledgeLevel, new Date()));
        } else {
            mCulaRepository.insertLibraryEntry(new LibraryEntry(nativeWord, foreignWord,
                    language, selectedKnowledgeLevel));
        }

        if (view.getId() == R.id.button_add_word_pair_return)
            finish();
        else {
            mBinding.editTextAddNativeWord.setText("");
            mBinding.editTextAddForeignWord.setText("");
            mBinding.editTextAddNativeWord.requestFocus();
            setKnowledgeLevelUI(Double.parseDouble(mSharedPreferences.getString(getString(R.string
                    .settings_default_knowledgeLevel_key), getString
                    (R.string.settings_default_knowledgeLevel_default))));
            Snackbar.make(mBinding.activityUpdateLibrary, R.string
                    .update_library_success_entry_added, Snackbar.LENGTH_SHORT).show();

        }
    }

    /**
     * Toggles the correct KnowledgeLevel radiobutton in the UI.
     *
     * @param knowledgeLevel The knowledgeLevel to display
     */
    private void setKnowledgeLevelUI(double knowledgeLevel) {
        selectedKnowledgeLevel = knowledgeLevel;

        if (knowledgeLevel < 1)
            mBinding.rbKnowledgeLevel1.toggle();
        else if (knowledgeLevel < 2)
            mBinding.rbKnowledgeLevel2.toggle();
        else if (knowledgeLevel < 3)
            mBinding.rbKnowledgeLevel3.toggle();
        else if (knowledgeLevel < 4)
            mBinding.rbKnowledgeLevel4.toggle();
        else
            mBinding.rbKnowledgeLevel5.toggle();

    }

}
