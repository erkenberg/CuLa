package com.sliebald.cula.ui.updateLesson;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

class UpdateLessonViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final int mEntryId;

    UpdateLessonViewModelFactory(int entryId) {
        mEntryId = entryId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new UpdateLessonViewModel(mEntryId);
    }
}
