package com.sliebald.cula.ui.updateLibrary;

import com.sliebald.cula.data.CulaRepository;
import com.sliebald.cula.data.database.Entities.LibraryEntry;
import com.sliebald.cula.utilities.InjectorUtils;
import com.sliebald.cula.utilities.PreferenceUtils;

import java.util.Date;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

/**
 * {@link ViewModel}  for the {@link UpdateLibraryFragment}.
 */
public class UpdateLibraryViewModel extends ViewModel {

    /**
     * The {@link LiveData} wrapped {@link LibraryEntry} if an existing entry is edited
     * (entryid !=-1).
     */
    private final LiveData<LibraryEntry> entry;

    /**
     * The {@link CulaRepository} for data access.
     */
    private CulaRepository mRepository;


    /**
     * Constructor.
     *
     * @param entryId Id of the {@link LibraryEntry} That should be loaded.
     */
    public UpdateLibraryViewModel(int entryId) {
        mRepository = InjectorUtils.provideRepository();
        // if an ID !=-1 is given as extra an existing entry should be loaded and updated instead of
        // creating a new one.
        if (entryId != -1)
            entry = InjectorUtils.provideRepository().getLibraryEntry(entryId);
        else
            entry = null;
    }

    /**
     * Returns the current {@link LibraryEntry}.
     *
     * @return The {@link LiveData} wrapped {@link LibraryEntry}
     */
    public LiveData<LibraryEntry> getEntry() {
        return entry;
    }

    /**
     * Commits the changes to the {@link CulaRepository}. If an existing {@link LibraryEntry} was
     * changed, it is updated, otherwise a new {@link LibraryEntry} is created.
     *
     * @param nativeWord             The native word of the {@link LibraryEntry}.
     * @param foreignWord            The foreign word of the {@link LibraryEntry}.
     * @param selectedKnowledgeLevel The chosen KnowledgeLevel of the {@link LibraryEntry}.
     */
    void commitEntry(String nativeWord, String foreignWord, double selectedKnowledgeLevel) {
        if (entry != null && entry.getValue() != null) {
            mRepository.updateLibraryEntry(new LibraryEntry(entry.getValue().getId(), nativeWord,
                    foreignWord, entry.getValue().getLanguage(), selectedKnowledgeLevel, new Date()));
        } else {
            mRepository.insertLibraryEntry(new LibraryEntry(nativeWord, foreignWord,
                    PreferenceUtils.getActiveLanguage(), selectedKnowledgeLevel));
        }

    }
}
