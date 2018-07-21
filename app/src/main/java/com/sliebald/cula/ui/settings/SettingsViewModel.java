package com.sliebald.cula.ui.settings;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.sliebald.cula.data.database.Entities.LanguageEntry;
import com.sliebald.cula.utilities.InjectorUtils;

import java.util.List;


public class SettingsViewModel extends ViewModel {

    private LiveData<List<LanguageEntry>> mLanguageEntries;

    /**
     * Constructor of the ViewModel.
     *
     */
    SettingsViewModel() {
        mLanguageEntries = InjectorUtils.provideRepository().getAllLanguageEntries();
    }

    public LiveData<List<LanguageEntry>> getLanguageEntries() {
        return mLanguageEntries;
    }

}
