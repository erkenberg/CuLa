package org.liebald.android.cula.ui.statistics;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.liebald.android.cula.data.CulaRepository;

public class StatisticsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final CulaRepository mCulaRepository;

    public StatisticsViewModelFactory(CulaRepository culaRepository) {
        mCulaRepository = culaRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new StatisticsViewModel(mCulaRepository);
    }
}
