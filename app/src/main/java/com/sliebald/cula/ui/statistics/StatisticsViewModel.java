package com.sliebald.cula.ui.statistics;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.sliebald.cula.data.CulaRepository;
import com.sliebald.cula.data.database.Pojos.StatisticsActivityEntry;
import com.sliebald.cula.data.database.Pojos.StatisticsLibraryWordCount;
import com.sliebald.cula.ui.updateLibrary.UpdateLibraryActivity;
import com.sliebald.cula.utilities.InjectorUtils;

import java.util.List;

/**
 * {@link ViewModel}  for the {@link UpdateLibraryActivity}.
 */
class StatisticsViewModel extends ViewModel {

    private final LiveData<List<StatisticsLibraryWordCount>> libraryCount;
    private final LiveData<List<StatisticsActivityEntry>> activity;

    /**
     * Constructor.
     */
    StatisticsViewModel() {
        CulaRepository mRepository = InjectorUtils.provideRepository();
        libraryCount = mRepository.getStatisticsLibraryCountByKnowledgeLevel();
        activity = mRepository.getStatisticsActivity();
    }

    public LiveData<List<StatisticsLibraryWordCount>> getLibraryCount() {
        return libraryCount;
    }

    public LiveData<List<StatisticsActivityEntry>> getActivity() {
        return activity;
    }
}
