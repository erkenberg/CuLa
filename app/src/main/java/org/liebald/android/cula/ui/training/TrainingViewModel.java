package org.liebald.android.cula.ui.training;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.liebald.android.cula.data.CulaRepository;
import org.liebald.android.cula.data.database.Entities.LibraryEntry;
import org.liebald.android.cula.ui.updateLibrary.UpdateLibraryActivity;

import java.util.List;

/**
 * Viewmodel for the {@link UpdateLibraryActivity}.
 */
public class TrainingViewModel extends ViewModel {

    private final LiveData<List<LibraryEntry>> entries;

    /**
     * Constructor.
     *
     * @param repository         Takes the CulaRepository
     * @param mLessonId
     * @param mAmount
     * @param mMinKnowledgeLevel
     * @param mMaxKnowledgeLevel Id of the {@link LibraryEntry} That should be loaded.
     */
    public TrainingViewModel(CulaRepository repository, int mAmount, int
            mMinKnowledgeLevel, int mMaxKnowledgeLevel, int mLessonId) {
        entries = repository.getTrainingEntries(mAmount, mMinKnowledgeLevel,
                mMaxKnowledgeLevel, mLessonId);
    }


    public LiveData<List<LibraryEntry>> getEntries() {
        return entries;
    }
}
