package org.liebald.android.cula.ui.settings;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.liebald.android.cula.data.database.Entities.LanguageEntry;
import org.liebald.android.cula.utilities.InjectorUtils;

import java.util.List;


public class SettingsFragmentViewModel extends ViewModel {

    private LiveData<List<LanguageEntry>> mLanguageEntries;

    /**
     * Constructor of the ViewModel.
     *
     */
    SettingsFragmentViewModel() {
        mLanguageEntries = InjectorUtils.provideRepository().getAllLanguageEntries();
    }

    public LiveData<List<LanguageEntry>> getLanguageEntries() {
        return mLanguageEntries;
    }

}
