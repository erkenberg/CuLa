package com.sliebald.cula.ui.updateLesson;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.sliebald.cula.data.CulaRepository;
import com.sliebald.cula.data.database.Entities.LessonEntry;
import com.sliebald.cula.data.database.Pojos.MappingPOJO;
import com.sliebald.cula.utilities.InjectorUtils;

import java.util.List;

/**
 * Viewmodel for the {@link UpdateLessonActivity}.
 */
class UpdateLessonViewModel extends ViewModel {

    private LiveData<LessonEntry> entry;
    private LiveData<List<MappingPOJO>> mapping;
    private CulaRepository mCulaRepository;

    /**
     * Constructor.
     *
     * @param entryId    Id of the {@link LessonEntry} which should be loaded.
     */
    UpdateLessonViewModel(int entryId) {
        mCulaRepository = InjectorUtils.provideRepository();
        updateViewModel(entryId);
    }

    /**
     * Update the {@link ViewModel} if entryId is  not -1.
     *
     * @param entryId Id of the {@link LessonEntry} which should be loaded.
     */
    void updateViewModel(int entryId) {
        if (entryId != -1) {
            entry = mCulaRepository.getLessonEntry(entryId);
            mapping = mCulaRepository.getMappingEntries(entryId);
        }
    }

    /**
     * Returns the current {@link LessonEntry}.
     *
     * @return The {@link LiveData} wrapped {@link LessonEntry}
     */
    LiveData<LessonEntry> getEntry() {
        return entry;
    }


    /**
     * Returns the {@link List} of {@link MappingPOJO}s for the current {@link LessonEntry}.
     *
     * @return The {@link LiveData} wrapped the {@link List} of {@link MappingPOJO}s.
     */
    LiveData<List<MappingPOJO>> getMapping() {
        return mapping;
    }
}
