package org.liebald.android.cula.ui.training;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.liebald.android.cula.data.CulaRepository;
import org.liebald.android.cula.data.database.Entities.LibraryEntry;
import org.liebald.android.cula.ui.updateLibrary.UpdateLibraryActivity;

import java.util.List;
import java.util.Objects;

/**
 * Viewmodel for the {@link UpdateLibraryActivity}.
 */
public class TrainingViewModel extends ViewModel {

    private final LiveData<List<LibraryEntry>> entries;

    private int currentIndex;

    private int lessonEntryId;

    /**
     * Constructor.
     *
     * @param repository         Takes the CulaRepository
     * @param mLessonId          The id of the lesson to load
     * @param mAmount            The amount of words to load
     * @param mMinKnowledgeLevel The maxKnowledgeLevel to load
     * @param mMaxKnowledgeLevel The minKnowledgeLevel to load
     */
    TrainingViewModel(CulaRepository repository, int mAmount, double
            mMinKnowledgeLevel, double mMaxKnowledgeLevel, int mLessonId) {
        if (mLessonId < 0) {
            entries = repository.getTrainingEntries(mAmount, mMinKnowledgeLevel,
                    mMaxKnowledgeLevel);
        } else {
            entries = repository.getTrainingEntries(mAmount, mMinKnowledgeLevel,
                    mMaxKnowledgeLevel, mLessonId);
        }
        currentIndex = -1;
        lessonEntryId = mLessonId;

    }

    public int getLessonEntryId() {
        return lessonEntryId;
    }


    /**
     * Return The {@link LiveData} {@link List} of all {@link LibraryEntry}s that should be
     * learned.
     *
     * @return The {@link LiveData} object.
     */
    public LiveData<List<LibraryEntry>> getEntries() {
        return entries;
    }

    /**
     * Checks whether there is a next LibraryEntry to learn.
     *
     * @return The next entry to learn.
     */
    public boolean hasNextEntry() {
        return Objects.requireNonNull(entries.getValue()).size() > currentIndex + 1;
    }

    /**
     * Returns the next {@link LibraryEntry} to learn.
     *
     * @return The next {@link LibraryEntry} to learn.
     */
    public LibraryEntry getNextEntry() {
        return Objects.requireNonNull(entries.getValue()).get(++currentIndex);
    }


    /**
     * Returns the current {@link LibraryEntry} to learn.
     *
     * @return The current {@link LibraryEntry} to learn.
     */
    public LibraryEntry getCurrentWord() {
        return Objects.requireNonNull(entries.getValue()).get(currentIndex);
    }

    /**
     * Returns the current position to learn.
     *
     * @return The current position to learn.
     */
    public int getLearningSetPosition() {
        return currentIndex + 1;
    }

    /**
     * Returns the size of the learning set.
     *
     * @return The size of the current learningset.
     */
    public int getLearningSetSize() {
        return Objects.requireNonNull(entries.getValue()).size();
    }

}
