package com.liebald.android.cula.ui.updateLibrary;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.liebald.android.cula.R;
import com.liebald.android.cula.data.CulaRepository;
import com.liebald.android.cula.data.database.LibraryEntry;
import com.liebald.android.cula.databinding.ActivityUpdateLibraryBinding;
import com.liebald.android.cula.utilities.InjectorUtils;

public class UpdateLibraryActivity extends AppCompatActivity {


    /**
     * When the intend passed to this activity contains a bundle with this extra
     * an existing {@link com.liebald.android.cula.data.database.LibraryEntry}
     * should be updated instead of adding a new one.
     * The id of the entry is passed as the value of the key.
     */
    public static final String BUNDLE_EXTRA_UPDATE_KEY = "LibraryEntryId";

    /**
     * The databinding for the Layout.
     */
    private ActivityUpdateLibraryBinding mBinding;
    private CulaRepository mCulaRepository;

    /**
     * The entryId of an entry that is updated. Only used when !=-1.
     */
    private int entryId = -1;

    /**
     * The currently selected knowledge level.
     */
    private double selectedKnowledgeLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_update_library);
        mCulaRepository = InjectorUtils.provideRepository(this);
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        int id;
        //TODO: use default knowledgeLevel from config
        mBinding.rbKnowledgeLevel3.toggle();
        if (intent.hasExtra(BUNDLE_EXTRA_UPDATE_KEY) && (id = intent.getIntExtra(BUNDLE_EXTRA_UPDATE_KEY, -1)) != -1) {
            mBinding.buttonAddWordPair.setVisibility(View.GONE);
            UpdateLibraryViewModelFactory viewModelFactory = new UpdateLibraryViewModelFactory(mCulaRepository, id);
            final UpdateLibraryViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(UpdateLibraryViewModel.class);
            viewModel.getEntry().observe(this, libraryEntry -> {
                viewModel.getEntry().removeObservers(this);
                mBinding.editTextAddForeignWord.setText(libraryEntry.getForeignWord());
                mBinding.editTextAddNativeWord.setText(libraryEntry.getNativeWord());
                if (libraryEntry.getKnowledgeLevel() < 1.5)
                    mBinding.rbKnowledgeLevel1.toggle();
                else if (libraryEntry.getKnowledgeLevel() < 2.5)
                    mBinding.rbKnowledgeLevel2.toggle();
                else if (libraryEntry.getKnowledgeLevel() < 3.5)
                    mBinding.rbKnowledgeLevel3.toggle();
                else if (libraryEntry.getKnowledgeLevel() < 4.5)
                    mBinding.rbKnowledgeLevel4.toggle();
                else
                    mBinding.rbKnowledgeLevel5.toggle();
                entryId = libraryEntry.getId();
                selectedKnowledgeLevel = libraryEntry.getKnowledgeLevel();
            });

        }

        mBinding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_knowledgeLevel_1:
                    selectedKnowledgeLevel = 1;
                    break;
                case R.id.rb_knowledgeLevel_2:
                    selectedKnowledgeLevel = 2;
                    break;
                case R.id.rb_knowledgeLevel_3:
                    selectedKnowledgeLevel = 3;
                    break;
                case R.id.rb_knowledgeLevel_4:
                    selectedKnowledgeLevel = 4;
                    break;
                case R.id.rb_knowledgeLevel_5:
                    selectedKnowledgeLevel = 5;
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
        //TODO: replacable by snackbar?
        if (nativeWord.isEmpty() || foreignWord.isEmpty()) {
            Toast.makeText(this, "Please fill in both words!", Toast.LENGTH_LONG).show();
            return;
        }
        if (entryId != -1) {
            //TODO: replace with update method?
            mCulaRepository.addLibraryEntry(new LibraryEntry(entryId, nativeWord, foreignWord, selectedKnowledgeLevel));
        } else {
            mCulaRepository.addLibraryEntry(new LibraryEntry(nativeWord, foreignWord, selectedKnowledgeLevel));
        }
        Log.d("TAG", "test " + selectedKnowledgeLevel);
        //TODO: replace by snackbar?
        if (view.getId() == R.id.button_add_word_pair_return)
            finish();
        else {
            mBinding.editTextAddNativeWord.setText("");
            mBinding.editTextAddForeignWord.setText("");
            Toast.makeText(this, "Added word pair to library", Toast.LENGTH_LONG).show();
        }
    }


}
