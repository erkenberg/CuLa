package com.sliebald.cula.ui.updateLibrary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.sliebald.cula.R;
import com.sliebald.cula.databinding.FragmentUpdateLibraryBinding;
import com.sliebald.cula.utilities.KeyboardUtils;
import com.sliebald.cula.utilities.PreferenceUtils;

public class UpdateLibraryFragment extends Fragment {

    /**
     * The data binding for the Layout.
     */
    private FragmentUpdateLibraryBinding mBinding;

    /**
     * {@link androidx.lifecycle.ViewModel} of this fragment.
     */
    private UpdateLibraryViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_update_library, container
                , false);

        UpdateLibraryFragmentArgs args = UpdateLibraryFragmentArgs.fromBundle(getArguments());

        int id = args.getLibraryEntryId();
        UpdateLibraryViewModelFactory viewModelFactory = new UpdateLibraryViewModelFactory(id);
        mViewModel =
                ViewModelProviders.of(this, viewModelFactory).get(UpdateLibraryViewModel.class);

        if (id != -1) {
            mBinding.buttonAddWordPair.setVisibility(View.GONE);
            mViewModel.getEntry().observe(this, libraryEntry -> {
                if (libraryEntry == null)
                    return;
                mViewModel.getEntry().removeObservers(this);
                mBinding.editTextAddForeignWord.setText(libraryEntry.getForeignWord());
                mBinding.editTextAddNativeWord.setText(libraryEntry.getNativeWord());
                setKnowledgeLevelUI(libraryEntry.getKnowledgeLevel());
            });
        } else {
            //on creating a new entry load the default day set in the settings.
            setKnowledgeLevelUI(PreferenceUtils.getDefaultKnowledgeLevel());
        }

        mBinding.buttonAddWordPair.setOnClickListener(v -> commitLibraryEntry(false));
        mBinding.buttonAddWordPairReturn.setOnClickListener(v -> commitLibraryEntry(true));

        return mBinding.getRoot();
    }

    /**
     * Returns the currently selected Knowledgelevel.
     *
     * @return The currently selected KnowledgeLevel as double.
     */
    private double getSelectedKnowledgeLevel() {
        switch (mBinding.radioGroup.getCheckedRadioButtonId()) {
            case R.id.rb_knowledgeLevel_1:
                return 0.5;
            case R.id.rb_knowledgeLevel_2:
                return 1.5;
            case R.id.rb_knowledgeLevel_3:
                return 2.5;
            case R.id.rb_knowledgeLevel_4:
                return 3.5;
            case R.id.rb_knowledgeLevel_5:
                return 4.5;
            default:
                return PreferenceUtils.getDefaultKnowledgeLevel();
        }
    }

    /**
     * Callback for the add/update word button.
     * Checks whether the words are not empty and triggers the library update.
     *
     * @param returnAfterSave If true return after saving the update.
     */
    private void commitLibraryEntry(boolean returnAfterSave) {
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

        mViewModel.commitEntry(nativeWord, foreignWord, getSelectedKnowledgeLevel());


        if (returnAfterSave)
            finishLibraryUpdate();
        else {
            mBinding.editTextAddNativeWord.setText("");
            mBinding.editTextAddForeignWord.setText("");
            mBinding.editTextAddNativeWord.requestFocus();
            setKnowledgeLevelUI(PreferenceUtils.getDefaultKnowledgeLevel());
            Snackbar.make(mBinding.activityUpdateLibrary, R.string
                    .update_library_success_entry_added, Snackbar.LENGTH_SHORT).show();

        }
    }

    /**
     * Return to the library overview fragment
     */
    private void finishLibraryUpdate() {
        KeyboardUtils.hideKeyboard(getContext(), getView());
        Navigation.findNavController(getView()).popBackStack();
    }

    /**
     * Toggles the correct KnowledgeLevel radiobutton in the UI.
     *
     * @param knowledgeLevel The knowledgeLevel to display
     */
    private void setKnowledgeLevelUI(double knowledgeLevel) {
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
