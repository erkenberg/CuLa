/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.liebald.android.cula.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import org.liebald.android.cula.data.CulaRepository;
import org.liebald.android.cula.data.database.CulaDatabase;
import org.liebald.android.cula.ui.lessons.LessonsViewModelFactory;
import org.liebald.android.cula.ui.library.LibraryViewModelFactory;
import org.liebald.android.cula.ui.quote.QuoteViewModelFactory;
import org.liebald.android.cula.ui.settings.SettingsViewModelFactory;
import org.liebald.android.cula.ui.startTraining.StartTrainingViewModelFactory;

/**
 * Provides static methods to inject the various classes needed for Sunshine
 */
public class InjectorUtils {

    /**
     * provides the {@link CulaRepository} for other classes for data access.
     *
     * @param context {@link Context} of the caller using the {@link CulaRepository}
     * @return The {@link CulaRepository}
     */
    public static CulaRepository provideRepository(Context context) {
        CulaDatabase database = CulaDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return CulaRepository.getInstance(database, executors, sharedPreferences, context.getApplicationContext()
        );
    }

    /**
     * Returns the {@link LibraryViewModelFactory} with access to the {@link CulaRepository}.
     *
     * @param context {@link Context} of the Fragment using the {@link LibraryViewModelFactory}
     * @return The {@link LibraryViewModelFactory}.
     */
    public static LibraryViewModelFactory provideLibraryViewModelFactory(Context context) {
        CulaRepository repository = provideRepository(context.getApplicationContext());
        return new LibraryViewModelFactory(repository);
    }

    /**
     * Returns the {@link LessonsViewModelFactory} with access to the {@link CulaRepository}.
     *
     * @param context {@link Context} of the Fragment using the {@link LessonsViewModelFactory}
     * @return The {@link LessonsViewModelFactory}.
     */
    public static LessonsViewModelFactory provideLessonsViewModelFactory(Context context) {
        CulaRepository repository = provideRepository(context.getApplicationContext());
        return new LessonsViewModelFactory(repository);
    }

    /**
     * Returns the {@link SettingsViewModelFactory} with access to the {@link CulaRepository}.
     *
     * @param context {@link Context} of the Fragment using the {@link SettingsViewModelFactory}
     * @return The {@link SettingsViewModelFactory}.
     */
    public static SettingsViewModelFactory provideLanguageViewModelFactory(Context context) {
        CulaRepository repository = provideRepository(context.getApplicationContext());
        return new SettingsViewModelFactory(repository);
    }

    /**
     * Returns the {@link QuoteViewModelFactory} with access to the {@link CulaRepository}.
     *
     * @param context {@link Context} of the Fragment using the {@link QuoteViewModelFactory}
     * @return The {@link QuoteViewModelFactory}.
     */
    public static QuoteViewModelFactory provideQuoteViewModelFactory(Context context) {
        CulaRepository repository = provideRepository(context.getApplicationContext());
        return new QuoteViewModelFactory(repository);
    }

    /**
     * Returns the {@link StartTrainingViewModelFactory} with access to the {@link CulaRepository}.
     *
     * @param context {@link Context} of the Fragment using the {@link StartTrainingViewModelFactory}
     * @return The {@link StartTrainingViewModelFactory}.
     */
    public static StartTrainingViewModelFactory provideStartTrainingViewModelFactory(Context context) {
        CulaRepository repository = provideRepository(context.getApplicationContext());
        return new StartTrainingViewModelFactory(repository);
    }
}