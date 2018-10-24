package com.sliebald.cula.ui.startTraining;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.sliebald.cula.data.database.Entities.LessonEntry;
import com.sliebald.cula.ui.updateLibrary.UpdateLibraryActivity;
import com.sliebald.cula.utilities.InjectorUtils;

import java.util.List;

/**
 * {@link ViewModel} for the {@link UpdateLibraryActivity}.
 */
public class StartTrainingViewModel extends ViewModel {

    private final LiveData<List<LessonEntry>> mLessonEntries;

    /**
     * Constructor.
     */
    public StartTrainingViewModel() {
        mLessonEntries = InjectorUtils.provideRepository().getAllLessonEntries();
    }

    /**
     * Returns all {@link LessonEntry}s for the current Language.
     *
     * @return The {@link LiveData} wrapped {@link List} of {@link LessonEntry}s.
     */
    public LiveData<List<LessonEntry>> getLessonEntries() {
        return mLessonEntries;
    }
}
