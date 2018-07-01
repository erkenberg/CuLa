package org.liebald.android.cula.ui.lessons;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import org.liebald.android.cula.data.CulaRepository;
import org.liebald.android.cula.data.database.Entities.LessonEntry;

import java.util.List;

/**
 * {@link ViewModel} for the {@link LessonsFragment}.
 */
public class LessonsFragmentViewModel extends ViewModel {

    private LiveData<List<LessonEntry>> mLessonEntries;
    private CulaRepository mCulaRepository;
    private LessonEntry latestDeletedEntry = null;


    private String mCurrentLanguage = "";

    /**
     * Constructor of the ViewModel.
     *
     * @param repository The repository needed for data operations.
     */
    LessonsFragmentViewModel(CulaRepository repository) {
        mCulaRepository = repository;
    }

    /**
     * Returns all {@link LessonEntry}s.
     *
     * @return All {@link LessonEntry}s as {@link List} in {@link LiveData}.
     */
    public LiveData<List<LessonEntry>> getLessonEntries() {
        return mLessonEntries;
    }

    /**
     * Remove the selected {@link LessonEntry} from the database.
     *
     * @param index Index of the selected index.
     */
    public void removeLessonEntry(int index) {
        if (mLessonEntries == null || mLessonEntries.getValue() == null)
            return;
        latestDeletedEntry = mLessonEntries.getValue().get(index);
        Log.d(LessonsFragmentViewModel.class.getSimpleName(), latestDeletedEntry.toString());
        mCulaRepository.deleteLessonEntry(latestDeletedEntry);
    }

    /**
     * Restore the latest deleted entry.
     */
    public void restoreLatestDeletedLessonEntry() {
        //TODO: currently the lesson mappings are still gone.
        mCulaRepository.insertLessonEntry(latestDeletedEntry);
    }

    /**
     * Manually trigger an update when the language was changed.
     */
    public void languageChanged() {
        mLessonEntries = mCulaRepository.getAllLessonEntries();
    }

    /**
     * Get the currently displayed language.
     *
     * @return The currently managed language
     */
    public String getCurrentLanguage() {
        return mCurrentLanguage;
    }

    /**
     * Set the currently displayed language.
     *
     * @param mCurrentLanguage The new language.
     */
    public void setCurrentLanguage(String mCurrentLanguage) {
        this.mCurrentLanguage = mCurrentLanguage;
    }
}
