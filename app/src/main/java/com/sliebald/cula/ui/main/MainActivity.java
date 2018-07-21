package com.sliebald.cula.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.sliebald.cula.R;
import com.sliebald.cula.data.database.Entities.LanguageEntry;
import com.sliebald.cula.services.UpdateQuoteJobService;
import com.sliebald.cula.ui.lessons.LessonsFragment;
import com.sliebald.cula.ui.library.LibraryFragment;
import com.sliebald.cula.ui.quote.QuoteFragment;
import com.sliebald.cula.ui.settings.SettingsFragment;
import com.sliebald.cula.ui.startTraining.StartTrainingFragment;
import com.sliebald.cula.ui.statistics.StatisticsFragment;

public class MainActivity extends AppCompatActivity {

    /**
     * Tag for logging.
     */
    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * The Navigation @{@link Drawer} from the MaterialDrawer library by Mike Penz.
     */
    private Drawer drawer;

    private static final int DRAWER_TRAIN_KEY = 0;
    private static final int DRAWER_LIBRARY_KEY = 1;
    private static final int DRAWER_SETTINGS_KEY = 2;
    private static final int DRAWER_QUOTE_KEY = 3;
    private static final int DRAWER_LESSONS_KEY = 4;
    private static final int DRAWER_STATISTICS_KEY = 5;

    private MainViewModel mViewModel;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //TODO: check if a language is selected, otherwise open settings and show a toast.
        //create the drawer and remember the `Drawer` result object
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        //check whether there is a language set as active.
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.nav_header_main_drawer)
                .withTranslucentStatusBar(false)
                .withDrawerWidthDp(250)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(DRAWER_QUOTE_KEY).withName
                                (R.string.drawer_label_quote).withIcon(FontAwesome.Icon
                                .faw_bookmark),
                        new PrimaryDrawerItem().withIdentifier(DRAWER_TRAIN_KEY).withName(R.string
                                .drawer_label_train).withIcon(FontAwesome.Icon.faw_pencil_alt),
                        new PrimaryDrawerItem().withIdentifier(DRAWER_LIBRARY_KEY).withName
                                (R.string.drawer_label_library).withIcon(FontAwesome.Icon.faw_book),
                        new PrimaryDrawerItem().withIdentifier(DRAWER_LESSONS_KEY).withName
                                (R.string.drawer_label_lessons).withIcon(FontAwesome.Icon
                                .faw_address_book2),
                        new PrimaryDrawerItem().withIdentifier(DRAWER_STATISTICS_KEY).withName(R
                                .string.drawer_label_statistics).withIcon(FontAwesome.Icon
                                .faw_chart_line),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withIdentifier(DRAWER_SETTINGS_KEY).withName(R
                                .string.drawer_label_settings).withIcon(FontAwesome.Icon.faw_cog)
                )
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    selectItem(drawerItem.getIdentifier(), false);
                    return true;
                })
                .withSelectedItem(DRAWER_QUOTE_KEY)
                .build();

        drawer.closeDrawer();
        selectItem(DRAWER_QUOTE_KEY, true);
        mViewModel.getActiveLanguage().observe(this, this::checkActiveLesson);


        // Start the jobservice for the quotes
        scheduleJobService();
    }


    /**
     * Checks that the active language is set correctly in the sharedPreferences. Also makes sure
     * that if no language is selected the user is forwarded to the settings and given a hint.
     *
     * @param languageEntry The currently active language in the database. If null none is set to
     *                      active.
     */
    private void checkActiveLesson(LanguageEntry languageEntry) {
        String preferencesLanguage = sharedPreferences.getString(getResources().getString(R.string
                .settings_select_language_key), "");
        if (languageEntry == null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getResources().getString(R.string
                    .settings_select_language_key), "");
            editor.apply();
        } else if (!languageEntry.getLanguage().equals(preferencesLanguage)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getResources().getString(R.string
                    .settings_select_language_key), languageEntry.getLanguage());
            editor.apply();
            Log.d(TAG, "Currently active language, preferences: " + sharedPreferences
                    .getString(getResources().getString(R.string.settings_select_language_key),
                            "") + "database: " + languageEntry.getLanguage());
        }

    }


    /**
     * Schedules a Job service to regularly update the motivational quote.
     */
    private void scheduleJobService() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));

        // Update the database immediately
        Job myInstantJob = dispatcher.newJobBuilder()
                .setService(UpdateQuoteJobService.class)
                .setTag("updateQuoteJobServiceNow")
                .build();
        dispatcher.mustSchedule(myInstantJob);

        // and also update it each 12-13 hours as recurring job
        Job recurringJob = dispatcher.newJobBuilder()
                .setService(UpdateQuoteJobService.class)
                .setTag("updateQuoteJobServiceRecurrent")
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        12 * 60 * 60,
                        13 * 60 * 60))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .build();
        dispatcher.mustSchedule(recurringJob);
    }

    /**
     * Handler for the clicked navigation drawer items. Loads the correct fragment in the activity.
     *
     * @param identifier The fragment to load.
     * @param firstCall Indicates whether this method is called for the first time.
     */
    private void selectItem(long identifier, boolean firstCall) {

        //TODO: proper fragment management on rotation
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment;
        if (!firstCall && identifier != DRAWER_SETTINGS_KEY && mViewModel.getActiveLanguage() !=
                null && mViewModel.getActiveLanguage().getValue() == null) {
            identifier = DRAWER_SETTINGS_KEY;
            drawer.setSelection(DRAWER_SETTINGS_KEY);
            Toast.makeText(this, "Please add/select a language.", Toast.LENGTH_LONG).show();

        }
        switch ((int) identifier) {
            case DRAWER_LIBRARY_KEY:
                fragment = new LibraryFragment();
                break;
            case DRAWER_SETTINGS_KEY:
                fragment = new SettingsFragment();
                break;
            case DRAWER_QUOTE_KEY:
                fragment = new QuoteFragment();
                break;
            case DRAWER_LESSONS_KEY:
                fragment = new LessonsFragment();
                break;
            case DRAWER_TRAIN_KEY:
                fragment = new StartTrainingFragment();
                break;
            case DRAWER_STATISTICS_KEY:
                fragment = new StatisticsFragment();
                break;
            default:
                return;
        }

        drawer.closeDrawer();

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

    }

}
