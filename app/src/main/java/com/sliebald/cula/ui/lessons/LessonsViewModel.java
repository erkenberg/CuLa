package com.sliebald.cula.ui.lessons;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import android.util.Log;

import com.sliebald.cula.data.CulaRepository;
import com.sliebald.cula.data.database.Entities.LessonEntry;
import com.sliebald.cula.utilities.InjectorUtils;

import java.util.List;

/**
 * {@link ViewModel} for the {@link LessonsFragment}.
 */
public class LessonsViewModel extends ViewModel {

    private final LiveData<List<LessonEntry>> mLessonEntries;
    private final CulaRepository mCulaRepository;
    private LessonEntry latestDeletedEntry = null;

    /**
     * Constructor of the ViewModel.
     */
    public LessonsViewModel() {
        mCulaRepository = InjectorUtils.provideRepository();
        mLessonEntries = mCulaRepository.getAllLessonEntries();
    }

    /**
     * Returns all {@link LessonEntry}s.
     *
     * @return All {@link LessonEntry}s as {@link List} in {@link LiveData}.
     */
    public LiveData<List<LessonEntry>> getLessonEntries() {
        return mLessonEntries;
    }

    /**
     * Remove the selected {@link LessonEntry} from the database.
     *
     * @param index Index of the selected index.
     */
    public void removeLessonEntry(int index) {
        if (mLessonEntries == null || mLessonEntries.getValue() == null)
            return;
        latestDeletedEntry = mLessonEntries.getValue().get(index);
        Log.d(LessonsViewModel.class.getSimpleName(), latestDeletedEntry.toString());
        mCulaRepository.deleteLessonEntry(latestDeletedEntry);
    }

//    /**
//     * Restore the latest deleted entry.
//     */
    //TODO: implement working version
//    public void restoreLatestDeletedLessonEntry() {
//        //TODO: currently the lesson mappings are still gone.
//        CulaRepository.OnLessonEntryAddedListener dummyListener = ids -> {
//        };
//        mCulaRepository.insertLessonEntry(dummyListener, latestDeletedEntry);
//    }

}
