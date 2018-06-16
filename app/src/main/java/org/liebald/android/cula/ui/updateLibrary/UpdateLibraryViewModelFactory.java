package org.liebald.android.cula.ui.updateLibrary;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.liebald.android.cula.data.CulaRepository;

public class UpdateLibraryViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final CulaRepository mCulaRepository;
    private final int mEntryId;

    public UpdateLibraryViewModelFactory(CulaRepository culaRepository, int entryId) {
        mCulaRepository = culaRepository;
        mEntryId = entryId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new UpdateLibraryViewModel(mCulaRepository, mEntryId);
    }
}
