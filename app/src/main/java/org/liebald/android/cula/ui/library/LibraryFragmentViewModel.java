package org.liebald.android.cula.ui.library;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import org.liebald.android.cula.data.CulaRepository;
import org.liebald.android.cula.data.database.LibraryEntry;

import java.util.List;


public class LibraryFragmentViewModel extends ViewModel {

    private LiveData<List<LibraryEntry>> mLibraryEntries;
    private CulaRepository mCulaRepository;
    private LibraryEntry latestDeletedEntry = null;

    /**
     * Constructor of the ViewModel.
     *
     * @param repository The repository needed for data operations.
     */
    LibraryFragmentViewModel(CulaRepository repository) {
        mCulaRepository = repository;
        mLibraryEntries = repository.getAllLibraryEntries();
    }

    public LiveData<List<LibraryEntry>> getLibraryEntries() {
        return mLibraryEntries;
    }

    public void removeLibraryEntry(int index) {
        if (mLibraryEntries == null || mLibraryEntries.getValue() == null)
            return;
        latestDeletedEntry = mLibraryEntries.getValue().get(index);
        Log.d(LibraryFragmentViewModel.class.getSimpleName(), latestDeletedEntry.toString());
        mCulaRepository.removeLibraryEntry(latestDeletedEntry);
    }

    public void restoreLatestDeletedLibraryEntry() {
        mCulaRepository.addLibraryEntry(latestDeletedEntry);
    }
}
