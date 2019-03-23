package com.sliebald.cula;

import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.sliebald.cula.data.database.Entities.LanguageEntry;
import com.sliebald.cula.databinding.MainActivityBinding;
import com.sliebald.cula.utilities.PreferenceUtils;

import java.util.HashSet;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    /**
     * Tag for logging.
     */
    private static final String TAG = MainActivity.class.getSimpleName();

    private NavController mNavController;

    private AppBarConfiguration mAppBarConfiguration;

    private MainActivityBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        Toolbar toolbar = mBinding.toolbar;
        MainViewModel mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        //check whether there is a language set as active.
        mViewModel.getActiveLanguage().observe(this, this::checkActiveLanguage);

        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        Set<Integer> tlds = new HashSet<>();

        tlds.add(R.id.startTraining_dest);
        tlds.add(R.id.library_dest);
        tlds.add(R.id.lessons_dest);
        tlds.add(R.id.statistics_dest);

        mAppBarConfiguration = new AppBarConfiguration.Builder(tlds).setDrawerLayout(mBinding.drawerLayout).build();

        setSupportActionBar(toolbar);
        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
        NavigationView navDrawer = mBinding.navView;
        NavigationUI.setupWithNavController(navDrawer, mNavController);

    }


    /**
     * Checks that the active language is set correctly in the sharedPreferences. Also makes sure
     * that if no language is selected the user is forwarded to the settings and given a hint.
     *
     * @param languageEntry The currently active language in the database. If null none is set to
     *                      active.
     */
    private void checkActiveLanguage(LanguageEntry languageEntry) {
        String preferencesLanguage =
                PreferenceUtils.getActiveLanguage();
        if (languageEntry == null) {
            PreferenceUtils.setActiveLanguage("");
        } else if (!languageEntry.getLanguage().equals(preferencesLanguage)) {
            PreferenceUtils.setActiveLanguage(languageEntry.getLanguage());
        }
        //reflect the active language to the toolbar subtitle
        if (languageEntry != null)
            mBinding.toolbar.setSubtitle(languageEntry.getLanguage());
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(mNavController, mAppBarConfiguration);
    }

}
