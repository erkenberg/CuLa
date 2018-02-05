package android.liebald.com.cula.ui.updateDictionary;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.liebald.com.cula.data.CulaRepository;

/**
 * Factory method that allows us to create a ViewModel with a constructor that takes a
 * {@link android.liebald.com.cula.data.CulaRepository}
 */
public class UpdateDictionaryViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final CulaRepository mRepository;

    public UpdateDictionaryViewModelFactory(CulaRepository repository) {
        this.mRepository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new UpdateDictionaryFragmentViewModel(mRepository);
    }
}
