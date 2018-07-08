package org.liebald.android.cula.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;

import org.liebald.android.cula.R;
import org.liebald.android.cula.ui.lessons.LessonsFragment;
import org.liebald.android.cula.ui.library.LibraryFragment;
import org.liebald.android.cula.ui.quote.QuoteFragment;
import org.liebald.android.cula.ui.settings.SettingsFragment;
import org.liebald.android.cula.ui.startTraining.StartTrainingFragment;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);

        //create the drawer and remember the `Drawer` result object
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.nav_header_main_drawer)
                .withTranslucentStatusBar(false)
                .withDrawerWidthDp(250)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(DRAWER_QUOTE_KEY).withName(R.string.drawer_label_quote).withIcon(FontAwesome.Icon.faw_bookmark),
                        new PrimaryDrawerItem().withIdentifier(DRAWER_TRAIN_KEY).withName(R.string.drawer_label_train).withIcon(FontAwesome.Icon.faw_pencil_alt),
                        new PrimaryDrawerItem().withIdentifier(DRAWER_LIBRARY_KEY).withName(R.string.drawer_label_library).withIcon(FontAwesome.Icon.faw_book),
                        new PrimaryDrawerItem().withIdentifier(DRAWER_LESSONS_KEY).withName(R.string.drawer_label_lessons).withIcon(FontAwesome.Icon.faw_address_book2),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withIdentifier(DRAWER_SETTINGS_KEY).withName(R.string.drawer_label_settings).withIcon(FontAwesome.Icon.faw_cog)
                )
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    selectItem(drawerItem.getIdentifier());
                    return true;
                })
                .withSelectedItem(DRAWER_TRAIN_KEY)
                .build();

        drawer.closeDrawer();
        selectItem(DRAWER_TRAIN_KEY);
    }


    /**
     * Handler for the clicked navigation drawer items. Loads the correct fragment in the activity.
     *
     * @param identifier The fragment to load.
     */
    private void selectItem(long identifier) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment;
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
