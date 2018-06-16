package org.liebald.android.cula.ui.library;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.liebald.android.cula.data.CulaRepository;

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link CulaRepository}
 */
public class LibraryViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final CulaRepository mRepository;

    public LibraryViewModelFactory(CulaRepository repository) {
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new LibraryFragmentViewModel(mRepository);
    }
}
