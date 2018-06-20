package org.liebald.android.cula.ui.settings;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.liebald.android.cula.data.CulaRepository;
import org.liebald.android.cula.data.database.Entities.LanguageEntry;

import java.util.List;


public class SettingsFragmentViewModel extends ViewModel {

    private LiveData<List<LanguageEntry>> mLanguageEntries;

    /**
     * Constructor of the ViewModel.
     *
     * @param repository The repository needed for data operations.
     */
    SettingsFragmentViewModel(CulaRepository repository) {
        mLanguageEntries = repository.getAllLanguageEntries();
    }

    public LiveData<List<LanguageEntry>> getLanguageEntries() {
        return mLanguageEntries;
    }

}
