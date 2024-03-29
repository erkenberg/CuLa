package com.sliebald.cula

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sliebald.cula.data.database.entities.LanguageEntry
import com.sliebald.cula.utilities.InjectorUtils

class MainViewModel : ViewModel() {
    val activeLanguage: LiveData<LanguageEntry?> = InjectorUtils.provideRepository().getActiveLanguage()
}