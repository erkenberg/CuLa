package org.liebald.android.cula.ui.quote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.liebald.android.cula.data.CulaRepository;
import org.liebald.android.cula.data.Quote;
import org.liebald.android.cula.ui.updateLibrary.UpdateLibraryActivity;

/**
 * Viewmodel for the {@link UpdateLibraryActivity}.
 */
public class QuoteViewModel extends ViewModel {

    private final LiveData<Quote> quote;

    /**
     * Constructor.
     *
     * @param repository Takes the CulaRepository
     */
    public QuoteViewModel(CulaRepository repository) {
        quote = repository.getQuote();
    }

    /**
     * Returns the current {@link Quote}.
     *
     * @return The {@link LiveData} wrapped {@link Quote}
     */
    public LiveData<Quote> getQuote() {
        return quote;
    }
}
