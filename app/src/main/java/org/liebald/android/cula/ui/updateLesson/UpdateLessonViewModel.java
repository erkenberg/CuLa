package org.liebald.android.cula.ui.updateLesson;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.liebald.android.cula.data.CulaRepository;
import org.liebald.android.cula.data.database.Entities.LessonEntry;
import org.liebald.android.cula.data.database.Entities.MappingPOJO;

import java.util.List;

/**
 * Viewmodel for the {@link UpdateLessonActivity}.
 */
public class UpdateLessonViewModel extends ViewModel {

    private final LiveData<LessonEntry> entry;
    private final LiveData<List<MappingPOJO>> mapping;

    /**
     * Constructor.
     *
     * @param repository Takes the CulaRepository
     * @param entryId    Id of the {@link LessonEntry} That should be loaded.
     */
    UpdateLessonViewModel(CulaRepository repository, int entryId) {
        entry = repository.getLessonEntry(entryId);
        mapping = repository.getMappingEntries(entryId);
    }

    /**
     * Returns the current {@link LessonEntry}.
     *
     * @return The {@link LiveData} wrapped {@link LessonEntry}
     */
    public LiveData<LessonEntry> getEntry() {
        return entry;
    }


    /**
     * Returns the {@link List} of {@link MappingPOJO}s for the current {@link LessonEntry}.
     *
     * @return The {@link LiveData} wrapped the {@link List} of {@link MappingPOJO}s.
     */
    public LiveData<List<MappingPOJO>> getMapping() {
        return mapping;
    }
}
