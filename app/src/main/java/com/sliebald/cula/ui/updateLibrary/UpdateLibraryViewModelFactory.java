package com.sliebald.cula.ui.updateLibrary;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

class UpdateLibraryViewModelFactory implements ViewModelProvider.Factory {

    private final int mEntryId;

    UpdateLibraryViewModelFactory(int entryId) {
        mEntryId = entryId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new UpdateLibraryViewModel(mEntryId);
    }
}
