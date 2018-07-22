package com.sliebald.cula.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

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
import com.sliebald.cula.databinding.MainActivityBinding;
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

    /**
     * Key for the Library entry in the navigation drawer.
     */
    private static final int DRAWER_LIBRARY_KEY = 1;

    /**
     * Key for the Settings entry in the navigation drawer.
     */
    private static final int DRAWER_SETTINGS_KEY = 2;

    /**
     * Key for the Quote entry in the navigation drawer.
     */
    private static final int DRAWER_QUOTE_KEY = 3;

    /**
     * Key for the Lesson entry in the navigation drawer.
     */
    private static final int DRAWER_LESSONS_KEY = 4;

    /**
     * Key for the Statistics entry in the navigation drawer.
     */
    private static final int DRAWER_STATISTICS_KEY = 5;

    /**
     * Key for the Training entry in the navigation drawer.
     */
    private static final int DRAWER_START_TRAINING_KEY = 6;

    /**
     * Key for the savedInstanceState for the selected item in the navigation drawer.
     */
    private static final String SAVED_INSTANCE_STATE_ACTIVE_DRAWER_ITEM_KEY = "activeDrawerItem";

    /**
     * Key for the savedInstanceState for the {@link Toolbar} title.
     */
    private static final String SAVED_INSTANCE_STATE_TOOLBAR_TITLE = "toolbarTitle";
    /**
     * Key for the savedInstanceState for the {@link Toolbar} subtitle.
     */
    private static final String SAVED_INSTANCE_STATE_TOOLBAR_SUBTITLE = "toolbarSubTitle";

    private MainViewModel mViewModel;

    private SharedPreferences sharedPreferences;

    /**
     * The data binding for the Layout.
     */
    private MainActivityBinding mBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //create the drawer and remember the `Drawer` result object
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        //check whether there is a language set as active.
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        long active_item = DRAWER_QUOTE_KEY;
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_INSTANCE_STATE_ACTIVE_DRAWER_ITEM_KEY))
                active_item = savedInstanceState.getLong
                        (SAVED_INSTANCE_STATE_ACTIVE_DRAWER_ITEM_KEY,
                                active_item);
            if (savedInstanceState.containsKey(SAVED_INSTANCE_STATE_TOOLBAR_TITLE))
                mBinding.toolbar.setTitle(savedInstanceState.getString
                        (SAVED_INSTANCE_STATE_TOOLBAR_TITLE));
            if (savedInstanceState.containsKey(SAVED_INSTANCE_STATE_TOOLBAR_SUBTITLE))
                mBinding.toolbar.setSubtitle(savedInstanceState.getString
                        (SAVED_INSTANCE_STATE_TOOLBAR_SUBTITLE));

        }

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
                        new PrimaryDrawerItem().withIdentifier(DRAWER_START_TRAINING_KEY)
                                .withName(R.string
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
                .withSelectedItem(active_item)
                .build();

        drawer.closeDrawer();
        if (savedInstanceState == null || !savedInstanceState.containsKey
                (SAVED_INSTANCE_STATE_ACTIVE_DRAWER_ITEM_KEY))
            selectItem(active_item, true);

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

        // Update the database immediately, only added if this job isn't running already.
//        Job myInstantJob = dispatcher.newJobBuilder()
//                .setService(UpdateQuoteJobService.class)
//                .setTag("updateQuoteJobService")
//                .setRecurring(false)
//                .build();
//        dispatcher.mustSchedule(myInstantJob);

        // and also update it each 12-13 hours as recurring job, only added if this job isn't
        // running already.
        //TODO: Recurring doesn't seem to work properly
        Job recurringJob = dispatcher.newJobBuilder()
                .setService(UpdateQuoteJobService.class)
                .setTag("updateQuoteJobService")
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        12 * 60 * 60,
                        13 * 60 * 60))
                .setReplaceCurrent(false)
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

        //TODO: really the best way to always create a new fragment each time?
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment;
        if (!firstCall && identifier != DRAWER_SETTINGS_KEY && mViewModel.getActiveLanguage() !=
                null && mViewModel.getActiveLanguage().getValue() == null) {
            drawer.setSelection(DRAWER_SETTINGS_KEY);
            Snackbar.make(mBinding.mainLinearLayout, R.string.settings_add_language_summary,
                    Snackbar.LENGTH_LONG).show();
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        String tag;
        switch ((int) identifier) {
            case DRAWER_LIBRARY_KEY:
                tag = LibraryFragment.TAG;
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null)
                    fragment = new LibraryFragment();
                Log.d(TAG, "test1");
                mBinding.toolbar.setTitle(R.string.drawer_label_library);
                break;
            case DRAWER_SETTINGS_KEY:
                tag = SettingsFragment.TAG;
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null)
                    fragment = new SettingsFragment();
                break;
            case DRAWER_QUOTE_KEY:
                tag = QuoteFragment.TAG;
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null)
                    fragment = new QuoteFragment();
                mBinding.toolbar.setTitle(R.string.drawer_label_quote);
                break;
            case DRAWER_LESSONS_KEY:
                tag = LessonsFragment.TAG;
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null)
                    fragment = new LessonsFragment();
                mBinding.toolbar.setTitle(R.string.drawer_label_lessons);
                break;
            case DRAWER_START_TRAINING_KEY:
                tag = StartTrainingFragment.TAG;
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null)
                    fragment = new StartTrainingFragment();
                mBinding.toolbar.setTitle(R.string.drawer_label_train);
                break;
            case DRAWER_STATISTICS_KEY:
                tag = StatisticsFragment.TAG;
                fragment = fragmentManager.findFragmentByTag(tag);
                if (fragment == null)
                    fragment = new StatisticsFragment();
                mBinding.toolbar.setTitle(R.string.drawer_label_statistics);
                break;
            default:
                return;
        }
        if (mViewModel.getActiveLanguage() != null && mViewModel.getActiveLanguage().getValue()
                != null && identifier != DRAWER_SETTINGS_KEY)
            mBinding.toolbar.setSubtitle(mViewModel.getActiveLanguage().getValue().getLanguage());

        drawer.closeDrawer();

        // Insert the fragment by replacing any existing fragment
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment, tag)
                .addToBackStack(null)
                .commit();
        fragmentManager.executePendingTransactions();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(SAVED_INSTANCE_STATE_ACTIVE_DRAWER_ITEM_KEY, drawer.getCurrentSelection());
        if (mBinding.toolbar.getTitle() != null)
            outState.putString(SAVED_INSTANCE_STATE_TOOLBAR_TITLE,
                    mBinding.toolbar.getTitle().toString());
        if (mBinding.toolbar.getSubtitle() != null)
            outState.putString(SAVED_INSTANCE_STATE_TOOLBAR_SUBTITLE,
                    mBinding.toolbar.getSubtitle().toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        }
    }

}
