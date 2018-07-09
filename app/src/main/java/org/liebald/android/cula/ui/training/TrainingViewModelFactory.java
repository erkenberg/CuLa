package org.liebald.android.cula.ui.training;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.liebald.android.cula.data.CulaRepository;

public class TrainingViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final CulaRepository mCulaRepository;
    private final int mLessonId;
    private final int mMinKnowledgeLevel;
    private final int mMaxKnowledgeLevel;
    private final int mAmount;


    public TrainingViewModelFactory(CulaRepository culaRepository, int number, int
            minKnowledgeLevel, int maxKnowledgeLevel, int lessonId) {
        mCulaRepository = culaRepository;
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
