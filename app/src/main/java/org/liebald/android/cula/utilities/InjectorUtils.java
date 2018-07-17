package org.liebald.android.cula.utilities;

import org.liebald.android.cula.MyApplication;
import org.liebald.android.cula.data.CulaRepository;
import org.liebald.android.cula.data.database.CulaDatabase;

/**
 * Provides Utility methods for injecting e.g. the {@link CulaRepository} in other classes (e.g.
 * the ViewModels).
 */
public class InjectorUtils {

    /**
     * Provides the {@link CulaRepository} for other classes for data access. Uses the
     * Application context provided by {@link MyApplication}.
     *
     * @return The {@link CulaRepository}
     */
    //TODO: remove context
    public static CulaRepository provideRepository() {
        CulaDatabase database = CulaDatabase.getInstance(MyApplication.getContext());
        AppExecutors executors = AppExecutors.getInstance();
        return CulaRepository.getInstance(database, executors);
    }



}