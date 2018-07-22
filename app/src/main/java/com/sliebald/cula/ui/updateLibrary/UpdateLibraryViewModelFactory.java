package com.sliebald.cula.ui.updateLibrary;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.sliebald.cula.data.CulaRepository;

class UpdateLibraryViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final int mEntryId;

    public UpdateLibraryViewModelFactory(CulaRepository culaRepository, int entryId) {
        mEntryId = entryId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new UpdateLibraryViewModel(mEntryId);
    }
}
