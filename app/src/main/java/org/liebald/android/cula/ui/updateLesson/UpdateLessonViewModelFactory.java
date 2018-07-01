package org.liebald.android.cula.ui.updateLesson;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.liebald.android.cula.data.CulaRepository;

public class UpdateLessonViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final CulaRepository mCulaRepository;
    private final int mEntryId;

    public UpdateLessonViewModelFactory(CulaRepository culaRepository, int entryId) {
        mCulaRepository = culaRepository;
        mEntryId = entryId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new UpdateLessonViewModel(mCulaRepository, mEntryId);
    }
}
