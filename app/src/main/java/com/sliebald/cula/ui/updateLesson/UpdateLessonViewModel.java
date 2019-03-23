package com.sliebald.cula.ui.updateLesson;

import android.util.Log;

import com.sliebald.cula.data.CulaRepository;
import com.sliebald.cula.data.database.Entities.LessonEntry;
import com.sliebald.cula.data.database.Entities.LessonMappingEntry;
import com.sliebald.cula.data.database.Pojos.MappingPOJO;
import com.sliebald.cula.utilities.InjectorUtils;
import com.sliebald.cula.utilities.PreferenceUtils;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

/**
 * {@link ViewModel}  for the {@link UpdateLessonFragment}.
 */
public class UpdateLessonViewModel extends ViewModel implements CulaRepository
        .OnLessonEntryAddedListener {

    /**
     * Tag of this activity.
     */
    private static final String TAG = UpdateLessonFragment.class.getSimpleName();
    private LiveData<LessonEntry> entry;
    private LiveData<List<MappingPOJO>> mapping;
    private final CulaRepository mCulaRepository;

    /**
     * The entryId of an entry that is updated. -1 means a new entry is being edited.
     */
    private MutableLiveData<Integer> lessonId;

    /**
     * Constructor.
     *
     * @param entryId    Id of the {@link LessonEntry} which should be loaded.
     */
    public UpdateLessonViewModel(int entryId) {
        mCulaRepository = InjectorUtils.provideRepository();
        lessonId = new MutableLiveData<>();
        lessonId.setValue(entryId);
        entry = Transformations.switchMap(lessonId, mCulaRepository::getLessonEntry);
        //mCulaRepository.getLessonEntry(entryId);
        mapping = Transformations.switchMap(lessonId, mCulaRepository::getMappingEntries);

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

    /**
     * Updates the currently edited lesson. If it is a new lesson (lessonId<0), creates a new
     * lesson.
     *
     * @param lessonName        Name of the lesson.
     * @param lessonDescription Description of the lesson.
     * @return True if a new lesson was created, false if a lesson was updated.
     */
    boolean addOrUpdateLesson(String lessonName, String lessonDescription) {
        String language = PreferenceUtils.getActiveLanguage();
        if (lessonId.getValue() < 0) {
            Log.d(TAG, "Updating existing lesson: " + lessonId);
            mCulaRepository.insertLessonEntry(this, new LessonEntry(lessonName,
                    lessonDescription, language));
        } else {
            Log.d(TAG, "Inserting new lesson");
            mCulaRepository.updateLessonEntry(new LessonEntry(lessonId.getValue(), lessonName,
                    lessonDescription, language));
//            updateViewModel(lessonId);
        }
        return lessonId.getValue() < 0;
    }

    @Override
    public void onLessonEntryAdded(long[] ids) {
        if (ids.length > 0) {
            lessonId.postValue((int) ids[0]);
            Log.d(TAG, "Added ID " + ids[0]);
        }
    }

    /**
     * Insert or deletes the selected {@link LessonMappingEntry}.
     *
     * @param insert If true insert the entry, otherwise delete it.
     * @param id     Id of the {@link LessonMappingEntry} to add or delete.
     */
    void insertOrDeleteLessonMappingEntry(boolean insert, int id) {
        if (entry.getValue() == null)
            return;
        if (insert) {
            mCulaRepository.insertLessonMappingEntry(new LessonMappingEntry(entry.getValue().getId(),
                    id));
        } else {
            mCulaRepository.deleteLessonMappingEntry(new LessonMappingEntry(entry.getValue().getId(), id));
        }
    }
}
