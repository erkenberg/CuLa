package com.liebald.android.cula.ui.updateDictionary;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.liebald.android.cula.data.CulaRepository;
import com.liebald.android.cula.data.database.DictionaryEntry;

import java.util.List;


public class UpdateDictionaryFragmentViewModel extends ViewModel {

    private LiveData<List<DictionaryEntry>> mDictionaryEntries;
    private CulaRepository mCulaRepository;
    private DictionaryEntry latestDeletedEntry = null;

    /**
     * Constructor of the ViewModel.
     *
     * @param repository The repository needed for data operations.
     */
    public UpdateDictionaryFragmentViewModel(CulaRepository repository) {
        mCulaRepository = repository;
        mDictionaryEntries = repository.getAllDictionaryEntries();
    }

    public LiveData<List<DictionaryEntry>> getDictionaryEntries() {
        return mDictionaryEntries;
    }

    public void addNewDictionaryEntry(@NonNull String newNativeWord, @NonNull String newForeignWord) {
        Log.d("test", "added Words " + newNativeWord + " " + newForeignWord);
        if (newNativeWord.isEmpty() || newForeignWord.isEmpty()) {
            return;
        }
        mCulaRepository.addDictionaryEntry(new DictionaryEntry(newNativeWord, newForeignWord));
    }

    public void removeDictionaryEntry(int index) {
        latestDeletedEntry = mDictionaryEntries.getValue().get(index);
        Log.d(UpdateDictionaryFragmentViewModel.class.getSimpleName(), latestDeletedEntry.toString());
        mCulaRepository.removeDictionaryEntry(latestDeletedEntry);
    }

    public void restoreLatestDeletedDictionaryEntry() {
        mCulaRepository.addDictionaryEntry(latestDeletedEntry);
    }
}
