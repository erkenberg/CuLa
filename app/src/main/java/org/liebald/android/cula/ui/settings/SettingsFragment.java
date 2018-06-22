package org.liebald.android.cula.ui.settings;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.liebald.android.cula.R;
import org.liebald.android.cula.data.CulaRepository;
import org.liebald.android.cula.data.database.Entities.LanguageEntry;
import org.liebald.android.cula.utilities.InjectorUtils;

import java.util.List;
import java.util.Objects;

/**
 * The settings/preferences Fragment for configuration of the app.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = SettingsFragment.class.getSimpleName();

    /**
     * The {@link SharedPreferences} used to store the preferences.
     */
    SharedPreferences sharedPreferences;

    /**
     * The {@link SettingsFragmentViewModel} for the settings.
     */
    private SettingsFragmentViewModel mViewModel;

    /**
     * The {@link ListPreference} for selecting the current language.
     */
    private ListPreference mLanguageListPreference;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.fragment_settings);
        if (getActivity() != null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mLanguageListPreference = (ListPreference) findPreference(getResources().getString(R.string.settings_select_language_key));
        Preference addLanguage = findPreference(getResources().getString(R.string.settings_add_language_key));
        addLanguage.setOnPreferenceClickListener(preference -> {
            showAddLanguageDialog();
            return true;
        });
        Preference deleteLanguage = findPreference(getResources().getString(R.string.settings_delete_language_key));
        deleteLanguage.setOnPreferenceClickListener(preference -> {
            deleteLanguageDialog();
            return true;
        });

        if (getContext() != null) {
            SettingsViewModelFactory factory = InjectorUtils.provideLanguageViewModelFactory(getContext());
            mViewModel = ViewModelProviders.of(getActivity(), factory).get(SettingsFragmentViewModel.class);
        }
        mViewModel.getLanguageEntries().observe(this, this::updateLanguageList);

        onSharedPreferenceChanged(sharedPreferences, getString(R.string.settings_select_language_key));

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
        builder.setPositiveButton(R.string.add, (dialog, which) -> addNewLanguageToDb(newLanguageEditText.getText().toString()));
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
        builder.show();
    }

    /**
     * Adds a new language to the App.
     */
    private void addNewLanguageToDb(String newLanguage) {
        if (newLanguage == null || newLanguage.length() < 2 || newLanguage.length() > 20) {
            Toast.makeText(getContext(), "Language must be between 2 and 20 characters ", Toast.LENGTH_SHORT).show();
            return;
        }
        CulaRepository repository = InjectorUtils.provideRepository(Objects.requireNonNull(getContext()));
        repository.addLanguageEntry(new LanguageEntry(newLanguage));

    }

    /**
     * Deletes the current language from the app, including all words. Asks for confirmation first.
     */
    private void deleteLanguageDialog() {
        Context context = Objects.requireNonNull(getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getResources().getString(R.string.settings_delete_language_title_warning));
        builder.setPositiveButton(R.string.ok, (dialog, which) -> deleteLanguageFromDb(mLanguageListPreference.getValue()));
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
        builder.show();

    }

    /**
     * Deletes the current language from the app, including all words.
     */
    private void deleteLanguageFromDb(String language) {
        CulaRepository repository = InjectorUtils.provideRepository(Objects.requireNonNull(getContext()));
        repository.deleteLanguageEntry(new LanguageEntry(language));
        //todo: select another language in the list.
    }

    private void updateLanguageList(List<LanguageEntry> languageEntries) {
        String[] entryValues = new String[languageEntries.size()];
        for (int i = 0; i < languageEntries.size(); i++) {
            entryValues[i] = languageEntries.get(i).getLanguage();
        }
        if (languageEntries.size() == 0)
            return;
        Log.d(TAG, "" + mLanguageListPreference.getValue());
        mLanguageListPreference.setEntries(entryValues);
        mLanguageListPreference.setEntryValues(entryValues);
        mLanguageListPreference.setSummary(mLanguageListPreference.getValue());
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        //check
        if (preference instanceof ListPreference && key.equals(getResources().getString(R.string.settings_select_language_key))) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(sharedPreferences.getString(key, getResources().getString(R.string.settings_select_language_default)));
            if (index >= 0) {
                preference.setSummary(listPreference.getEntries()[index]);
                Log.d(TAG, "SharedPreferences was set to: " + listPreference.getEntries()[index]);
            }

        } else {
            //todo: implement logic for other preferences
            preference.setSummary(sharedPreferences.getString(key, key));
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
