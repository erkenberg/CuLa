package org.liebald.android.cula.ui.library;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import org.liebald.android.cula.data.CulaRepository;
import org.liebald.android.cula.data.database.LibraryEntry;

import java.util.List;

/**
 * {@link ViewModel} for the {@link LibraryFragment}.
 */
public class LibraryFragmentViewModel extends ViewModel {

    private LiveData<List<LibraryEntry>> mLibraryEntries;
    private CulaRepository mCulaRepository;
    private LibraryEntry latestDeletedEntry = null;


    private String mCurrentLanguage = "";

    /**
     * Constructor of the ViewModel.
     *
     * @param repository The repository needed for data operations.
     */
    LibraryFragmentViewModel(CulaRepository repository) {
        mCulaRepository = repository;
//        mLibraryEntries = repository.getAllLibraryEntries();
    }

    /**
     * Returns all {@link LibraryEntry}s.
     *
     * @return All {@link LibraryEntry}s as {@link List} in {@link LiveData}.
     */
    public LiveData<List<LibraryEntry>> getLibraryEntries() {
        return mLibraryEntries;
    }

    /**
     * Remove the selected {@link LibraryEntry} from the database.
     *
     * @param index Index of the selected index.
     */
    public void removeLibraryEntry(int index) {
        if (mLibraryEntries == null || mLibraryEntries.getValue() == null)
            return;
        latestDeletedEntry = mLibraryEntries.getValue().get(index);
        Log.d(LibraryFragmentViewModel.class.getSimpleName(), latestDeletedEntry.toString());
        mCulaRepository.removeLibraryEntry(latestDeletedEntry);
    }

    /**
     * Restore the latest deleted entry.
     */
    public void restoreLatestDeletedLibraryEntry() {
        mCulaRepository.addLibraryEntry(latestDeletedEntry);
    }

    /**
     * Manually trigger an update when the language was changed.
     */
    public void languageChanged() {
        mLibraryEntries = mCulaRepository.getAllLibraryEntries();
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
