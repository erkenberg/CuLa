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
import org.liebald.android.cula.ui.library.LibraryFragment;
import org.liebald.android.cula.ui.settings.SettingsFragment;
import org.liebald.android.cula.ui.train.CardBoxFragment;

public class MainActivity extends AppCompatActivity {

    /**
     * Tag for logging.
     */
    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * The Navigation @{@link Drawer} from the MaterialDrawer library by Mike Penz.
     */
    private Drawer drawer;

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
                        new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_label_train).withIcon(FontAwesome.Icon.faw_pencil_alt),
                        new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_label_library).withIcon(FontAwesome.Icon.faw_book),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withIdentifier(3).withName(R.string.drawer_label_settings).withIcon(FontAwesome.Icon.faw_cog)
                )
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    selectItem(drawerItem.getIdentifier());
                    return true;
                })
                .withSelectedItem(3)
                .build();

        drawer.closeDrawer();
        selectItem(3);
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
            case 2:
                fragment = new LibraryFragment();
                break;
            case 3:
                fragment = new SettingsFragment();
                break;
            default:
                fragment = new CardBoxFragment();
                Bundle args = new Bundle();
                args.putString("text", Long.toString(identifier));
                fragment.setArguments(args);
        }

        drawer.closeDrawer();

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

    }

}
