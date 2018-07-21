package com.sliebald.android.cula.ui.library;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.sliebald.android.cula.data.CulaRepository;
import com.sliebald.android.cula.data.database.Entities.LibraryEntry;
import com.sliebald.android.cula.utilities.InjectorUtils;

import java.util.List;

/**
 * {@link ViewModel} for the {@link LibraryFragment}.
 */
public class LibraryViewModel extends ViewModel {

    private LiveData<List<LibraryEntry>> mLibraryEntries;
    private CulaRepository mCulaRepository;
    private LibraryEntry latestDeletedEntry = null;

    /**
     * Constructor of the ViewModel.
     *
     */
    LibraryViewModel() {
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
        latestDeletedEntry = mLibraryEntries.getValue().get(index);
        Log.d(LibraryViewModel.class.getSimpleName(), latestDeletedEntry.toString());
        mCulaRepository.deleteLibraryEntry(latestDeletedEntry);
    }

    /**
     * Restore the latest deleted entry.
     */
    public void restoreLatestDeletedLibraryEntry() {
        mCulaRepository.insertLibraryEntry(latestDeletedEntry);
    }

}
