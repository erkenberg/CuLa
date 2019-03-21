package com.sliebald.cula.ui.training;

import com.sliebald.cula.data.CulaRepository;
import com.sliebald.cula.data.database.Pojos.TrainingData;
import com.sliebald.cula.utilities.InjectorUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

class TrainingViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final CulaRepository mCulaRepository;
    private final TrainingData mTrainingData;


    TrainingViewModelFactory(TrainingData trainingData) {
        mCulaRepository = InjectorUtils.provideRepository();
        mTrainingData = trainingData;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new TrainingViewModel(mCulaRepository, mTrainingData);
    }
}
