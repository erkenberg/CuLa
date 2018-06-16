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

import org.liebald.android.cula.data.CulaRepository;
import org.liebald.android.cula.data.database.CulaDatabase;
import org.liebald.android.cula.ui.library.LibraryViewModelFactory;

/**
 * Provides static methods to inject the various classes needed for Sunshine
 */
public class InjectorUtils {

    public static CulaRepository provideRepository(Context context) {
        CulaDatabase database = CulaDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return CulaRepository.getInstance(database.libraryDao(), executors);
    }

    public static LibraryViewModelFactory provideLibraryViewModelFactory(Context context) {
        CulaRepository repository = provideRepository(context.getApplicationContext());
        return new LibraryViewModelFactory(repository);
    }
}