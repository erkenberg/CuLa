package com.sliebald.cula.ui.settings;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.sliebald.cula.R;
import com.sliebald.cula.data.CulaRepository;
import com.sliebald.cula.data.database.Entities.LanguageEntry;
import com.sliebald.cula.utilities.InjectorUtils;

import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

/**
 * The settings/preferences Fragment for configuration of the app.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences
        .OnSharedPreferenceChangeListener {

    public static final String TAG = SettingsFragment.class.getSimpleName();

    /**
     * The {@link SettingsViewModel} for the settings.
     */
    private SettingsViewModel mViewModel;

    /**
     * The {@link ListPreference} for selecting the current language.
     */
    private ListPreference mLanguageListPreference;

    private Preference mDeleteLanguage;

    public SettingsFragment() {
        // Required empty public constructor
    }

    private CulaRepository mRepository;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // set the correct preference layout
        addPreferencesFromResource(R.xml.fragment_settings);
        mRepository = InjectorUtils.provideRepository();

        // get the ListPreference for the language settings (select, add, delete)
        mLanguageListPreference = (ListPreference) findPreference(getResources().getString(R
                .string.settings_select_language_key));
        Preference addLanguage = findPreference(getResources().getString(R.string
                .settings_add_language_key));
        mDeleteLanguage = findPreference(getResources().getString(R.string
                .settings_delete_language_key));

        // set onClick listener for adding/deleting language buttons
        addLanguage.setOnPreferenceClickListener(preference -> {
            showAddLanguageDialog();
            return true;
        });
        mDeleteLanguage.setOnPreferenceClickListener(preference -> {
            deleteLanguageDialog();
            return true;
        });

        // get the ViewModel
        if (getActivity() != null && getContext() != null) {
            mViewModel = ViewModelProviders.of(getActivity()).get(SettingsViewModel.class);
        }
        //Load the language entries and update the ListPreference
        mViewModel.getLanguageEntries().observe(this, this::updateLanguageList);

    }

    /**
     * Shows a dialog and asks the user to write the language he wants to add.
     */
    private void showAddLanguageDialog() {
        Context context = Objects.requireNonNull(getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getResources().getString(R.string.settings_add_language_title));
        final EditText newLanguageEditText = new EditText(context);
        newLanguageEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(newLanguageEditText);
        builder.setPositiveButton(R.string.add, (dialog, which) -> addNewLanguageToDb
                (newLanguageEditText.getText().toString()));
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
        builder.show();
    }

    /**
     * Adds a new language to the App.
     */
    private void addNewLanguageToDb(String newLanguage) {
        if (newLanguage == null || newLanguage.length() < 2 || newLanguage.length() > 20) {
            Snackbar snackbar = Snackbar
                    .make(Objects.requireNonNull(getView()), R.string
                            .settings_warning_invalid_language_length, Snackbar
                            .LENGTH_LONG);
            snackbar.show();
            return;
        }
        mRepository.insertLanguageEntry(new LanguageEntry(newLanguage, true));
    }

    /**
     * Deletes the current language from the app, including all words. Asks for confirmation first.
     */
    private void deleteLanguageDialog() {
        Context context = Objects.requireNonNull(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getResources().getString(R.string.settings_delete_language_title_warning));
        builder.setPositiveButton(R.string.ok, (dialog, which) -> deleteLanguageFromDb
                (mLanguageListPreference.getValue()));
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
        builder.show();

    }

    /**
     * Deletes the current language from the app, including all words.
     */
    private void deleteLanguageFromDb(String language) {
        CulaRepository repository = InjectorUtils.provideRepository();
        repository.deleteLanguageEntry(new LanguageEntry(language, false));
    }

    /**
     * Updates the {@link ListPreference} when the list of entries was changed in the database.
     *
     * @param languageEntries the new List of entries that should be shown.
     */
    private void updateLanguageList(List<LanguageEntry> languageEntries) {
        int selected = 0;
        if (languageEntries.size() == 0) {
            mLanguageListPreference.setSummary(getResources().getString(R.string
                    .settings_select_language_default));
            mLanguageListPreference.setEntries(new String[]{getResources().getString(R.string
                    .settings_select_language_default)});
            mLanguageListPreference.setEnabled(false);
            mDeleteLanguage.setEnabled(false);
        } else {
            String[] entryValues = new String[languageEntries.size()];
            for (int i = 0; i < languageEntries.size(); i++) {
                entryValues[i] = languageEntries.get(i).getLanguage();
                if (languageEntries.get(i).isActive())
                    selected = i;
                Log.d(TAG, "Language: " + languageEntries.get(i).getLanguage() + " active: " +
                        languageEntries.get(i).isActive());
            }
            mDeleteLanguage.setEnabled(true);
            mLanguageListPreference.setEnabled(true);
            mLanguageListPreference.setEntries(entryValues);
            mLanguageListPreference.setValue(entryValues[selected]);
            mLanguageListPreference.setEntryValues(entryValues);
            mLanguageListPreference.setSummary(mLanguageListPreference.getValue());
            Log.d(TAG, "Update LanguageList: " + mLanguageListPreference.getValue() + " amount " +
                    "of languages: " + languageEntries.size());
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        //check which preference changed and update the summary correctly.
        if (preference instanceof ListPreference && key.equals(getResources().getString(R.string
                .settings_select_language_key))) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(sharedPreferences.getString(key,
                    getResources().getString(R.string.settings_select_language_default)));
            if (index >= 0 && index < listPreference.getEntries().length) {
                mRepository.setActiveLanguage(listPreference.getEntries()[index].toString());
                listPreference.setSummary(listPreference.getEntries()[index]);
                listPreference.setValue(listPreference.getEntries()[index].toString());
                Log.d(TAG, "SharedPreferences was set to: " + listPreference.getEntries()[index]);
            }
        } else if (preference instanceof ListPreference && key.equals(getResources().getString(R
                .string.settings_default_knowledgeLevel_key))) {
            ListPreference listPreference = (ListPreference) preference;
            preference.setSummary(listPreference.getEntry());
        } else if (preference instanceof ListPreference && key.equals(getResources().getString(R
                .string.settings_reward_correct_training_key))) {
            ListPreference listPreference = (ListPreference) preference;
            preference.setSummary(listPreference.getEntry());
        } else if (preference instanceof ListPreference && key.equals(getResources().getString(R
                .string.settings_punish_wrong_training_key))) {
            ListPreference listPreference = (ListPreference) preference;
            preference.setSummary(listPreference.getEntry());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
