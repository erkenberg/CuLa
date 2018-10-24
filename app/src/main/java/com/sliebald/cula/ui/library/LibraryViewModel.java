package com.sliebald.cula.ui.library;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import android.util.Log;

import com.sliebald.cula.data.CulaRepository;
import com.sliebald.cula.data.database.Entities.LibraryEntry;
import com.sliebald.cula.utilities.InjectorUtils;

import java.util.List;

/**
 * {@link ViewModel} for the {@link LibraryFragment}.
 */
public class LibraryViewModel extends ViewModel {

    private final LiveData<List<LibraryEntry>> mLibraryEntries;
    private final CulaRepository mCulaRepository;
    private LibraryEntry latestDeletedEntry = null;

    /**
     * Constructor of the ViewModel.
     *
     */
    public LibraryViewModel() {
        mCulaRepository = InjectorUtils.provideRepository();
        mLibraryEntries = mCulaRepository.getAllLibraryEntries();
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
        if (index < mLibraryEntries.getValue().size()) {
            latestDeletedEntry = mLibraryEntries.getValue().get(index);
            Log.d(LibraryViewModel.class.getSimpleName(), latestDeletedEntry.toString());
            mCulaRepository.deleteLibraryEntry(latestDeletedEntry);
        }
    }

    /**
     * Restore the latest deleted entry.
     */
    public void restoreLatestDeletedLibraryEntry() {
        mCulaRepository.insertLibraryEntry(latestDeletedEntry);
    }

}
