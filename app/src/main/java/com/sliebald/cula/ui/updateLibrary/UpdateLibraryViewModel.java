package com.sliebald.cula.ui.updateLibrary;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.sliebald.cula.data.database.Entities.LibraryEntry;
import com.sliebald.cula.utilities.InjectorUtils;

/**
 * {@link ViewModel}  for the {@link UpdateLibraryActivity}.
 */
public class UpdateLibraryViewModel extends ViewModel {

    private final LiveData<LibraryEntry> entry;

    /**
     * Constructor.
     *
     * @param entryId    Id of the {@link LibraryEntry} That should be loaded.
     */
    public UpdateLibraryViewModel(int entryId) {
        entry = InjectorUtils.provideRepository().getLibraryEntry(entryId);
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
