package com.sliebald.cula.ui.quote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.sliebald.cula.data.database.Entities.QuoteEntry;
import com.sliebald.cula.ui.updateLibrary.UpdateLibraryActivity;
import com.sliebald.cula.utilities.InjectorUtils;

/**
 * {@link ViewModel} for the {@link UpdateLibraryActivity}.
 */
class QuoteViewModel extends ViewModel {

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
