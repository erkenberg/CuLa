package com.sliebald.cula.ui.training;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.sliebald.cula.data.CulaRepository;
import com.sliebald.cula.utilities.InjectorUtils;

class TrainingViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final CulaRepository mCulaRepository;
    private final int mLessonId;
    private final double mMinKnowledgeLevel;
    private final double mMaxKnowledgeLevel;
    private final int mAmount;


    TrainingViewModelFactory(int number, double
            minKnowledgeLevel, double maxKnowledgeLevel, int lessonId) {
        mCulaRepository = InjectorUtils.provideRepository();
        mLessonId = lessonId;
        mMinKnowledgeLevel = minKnowledgeLevel;
        mMaxKnowledgeLevel = maxKnowledgeLevel;
        mAmount = number;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new TrainingViewModel(mCulaRepository, mAmount, mMinKnowledgeLevel,
                mMaxKnowledgeLevel, mLessonId);
    }
}
