package com.sliebald.android.cula.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.sliebald.android.cula.data.database.Entities.LanguageEntry;
import com.sliebald.android.cula.utilities.InjectorUtils;


public class MainViewModel extends ViewModel {

    private LiveData<LanguageEntry> mActiveLanguage;

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
