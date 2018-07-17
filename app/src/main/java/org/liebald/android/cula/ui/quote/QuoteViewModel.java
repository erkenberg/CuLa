package org.liebald.android.cula.ui.quote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import org.liebald.android.cula.data.database.Entities.QuoteEntry;
import org.liebald.android.cula.ui.updateLibrary.UpdateLibraryActivity;
import org.liebald.android.cula.utilities.InjectorUtils;

/**
 * Viewmodel for the {@link UpdateLibraryActivity}.
 */
public class QuoteViewModel extends ViewModel {

    private final LiveData<QuoteEntry> quote;

    /**
     * Constructor.
     *
     */
    public QuoteViewModel() {
        quote = InjectorUtils.provideRepository().getQuote();
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
