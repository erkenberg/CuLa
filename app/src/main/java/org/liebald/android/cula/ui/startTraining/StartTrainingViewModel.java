package org.liebald.android.cula.ui.startTraining;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.liebald.android.cula.data.CulaRepository;
import org.liebald.android.cula.data.database.Entities.LessonEntry;
import org.liebald.android.cula.ui.updateLibrary.UpdateLibraryActivity;

import java.util.List;

/**
 * {@link ViewModel} for the {@link UpdateLibraryActivity}.
 */
public class StartTrainingViewModel extends ViewModel {

    private LiveData<List<LessonEntry>> mLessonEntries;

    /**
     * Constructor.
     *
     * @param repository Takes the CulaRepository
     */
    StartTrainingViewModel(CulaRepository repository) {
        mLessonEntries = repository.getAllLessonEntries();
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
