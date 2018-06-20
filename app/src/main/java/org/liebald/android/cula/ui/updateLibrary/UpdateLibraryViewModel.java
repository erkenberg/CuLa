package org.liebald.android.cula.ui.updateLibrary;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.liebald.android.cula.data.CulaRepository;
import org.liebald.android.cula.data.database.Entities.LibraryEntry;

/**
 * Viewmodel for the {@link UpdateLibraryActivity}.
 */
public class UpdateLibraryViewModel extends ViewModel {

    private final LiveData<LibraryEntry> entry;

    /**
     * Constructor.
     *
     * @param repository Takes the CulaRepository
     * @param entryId    Id of the {@link LibraryEntry} That should be loaded.
     */
    public UpdateLibraryViewModel(CulaRepository repository, int entryId) {
        entry = repository.getLibraryEntry(entryId);
    }

    /**
     * Returns the current {@link LibraryEntry}.
     *
     * @return The {@link LiveData} wrapped {@link LibraryEntry}
     */
    public LiveData<LibraryEntry> getEntry() {
        return entry;
    }
}
