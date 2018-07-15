package org.liebald.android.cula.ui.statistics;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.liebald.android.cula.data.CulaRepository;
import org.liebald.android.cula.data.database.Pojos.StatisticsLibraryWordCount;
import org.liebald.android.cula.ui.updateLibrary.UpdateLibraryActivity;

import java.util.List;

/**
 * Viewmodel for the {@link UpdateLibraryActivity}.
 */
public class StatisticsViewModel extends ViewModel {

    private LiveData<List<StatisticsLibraryWordCount>> libraryCount;

    private CulaRepository mRepository;

    /**
     * Constructor.
     *
     * @param repository Takes the CulaRepository
     */
    StatisticsViewModel(CulaRepository repository) {
        mRepository = repository;
        libraryCount = mRepository.getStatisticsLibraryCountByKnowledgeLevel();
    }

    public LiveData<List<StatisticsLibraryWordCount>> getLibraryCount() {
        return libraryCount;
    }

}
