package android.liebald.com.cula.ui.updateDictionary;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.liebald.com.cula.data.CulaRepository;
import android.liebald.com.cula.data.database.DictionaryEntry;

import java.util.List;


public class UpdateDictionaryFragmentViewModel extends ViewModel {

    private LiveData<List<DictionaryEntry>> mDictionaryEntries;

    public UpdateDictionaryFragmentViewModel(CulaRepository repository) {
        mDictionaryEntries = repository.getAllDictionaryEntries();
    }

    public LiveData<List<DictionaryEntry>> getDictionaryEntries() {
        return mDictionaryEntries;
    }
}
