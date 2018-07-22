package com.sliebald.cula.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.sliebald.cula.data.database.Entities.LanguageEntry;
import com.sliebald.cula.utilities.InjectorUtils;


class MainViewModel extends ViewModel {

    private final LiveData<LanguageEntry> mActiveLanguage;

    /**
     * Constructor of the ViewModel.
     */
    MainViewModel() {
        mActiveLanguage = InjectorUtils.provideRepository().getActiveLanguage();
    }

    public LiveData<LanguageEntry> getActiveLanguage() {
        return mActiveLanguage;
    }

}
