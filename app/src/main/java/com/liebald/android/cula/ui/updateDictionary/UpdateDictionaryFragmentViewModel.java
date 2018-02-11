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

    public void addNewUser(@NonNull String newNativeWord, @NonNull String newForeignWord) {
        Log.d("test", "added Words " + newNativeWord + " " + newForeignWord);
        mCulaRepository.addDictionaryEntry(new DictionaryEntry(newNativeWord, newForeignWord));
    }
}
