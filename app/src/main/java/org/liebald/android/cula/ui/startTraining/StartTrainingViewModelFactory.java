package org.liebald.android.cula.ui.startTraining;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.liebald.android.cula.data.CulaRepository;

public class StartTrainingViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final CulaRepository mCulaRepository;

    public StartTrainingViewModelFactory(CulaRepository culaRepository) {
        mCulaRepository = culaRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new StartTrainingViewModel(mCulaRepository);
    }
}
