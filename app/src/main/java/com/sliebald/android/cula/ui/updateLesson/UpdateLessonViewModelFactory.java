package com.sliebald.android.cula.ui.updateLesson;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class UpdateLessonViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final int mEntryId;

    public UpdateLessonViewModelFactory(int entryId) {
        mEntryId = entryId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new UpdateLessonViewModel(mEntryId);
    }
}
