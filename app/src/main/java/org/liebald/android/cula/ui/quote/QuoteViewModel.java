package org.liebald.android.cula.ui.quote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.liebald.android.cula.data.CulaRepository;
import org.liebald.android.cula.data.database.Entities.QuoteEntry;
import org.liebald.android.cula.ui.updateLibrary.UpdateLibraryActivity;

/**
 * Viewmodel for the {@link UpdateLibraryActivity}.
 */
public class QuoteViewModel extends ViewModel {

    private final LiveData<QuoteEntry> quote;

    /**
     * Constructor.
     *
     * @param repository Takes the CulaRepository
     */
    public QuoteViewModel(CulaRepository repository) {
        quote = repository.getQuote();
    }

    /**
     * Returns the current {@link QuoteEntry}.
     *
     * @return The {@link LiveData} wrapped {@link QuoteEntry}
     */
    public LiveData<QuoteEntry> getQuote() {
        return quote;
    }
}
