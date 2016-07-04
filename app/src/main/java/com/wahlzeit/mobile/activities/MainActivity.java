package com.wahlzeit.mobile.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.adapters.NavDrawerListAdapter;
import com.wahlzeit.mobile.fragments.FragmentFactory;
import com.wahlzeit.mobile.fragments.Fragments;
import com.wahlzeit.mobile.fragments.HomeFragment;
import com.wahlzeit.mobile.fragments.ShowFragment;
import com.wahlzeit.mobile.fragments.TellFragment;
import com.wahlzeit.mobile.model.ModelManager;
import com.wahlzeit.mobile.model.NavDrawerItem;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * An activity to manage navigation (nav drawer) and fragments lifecycle
 */
public class MainActivity extends BaseActivity {

    private NavDrawerListAdapter mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private String mActivityTitle;
    // used to store app title
    private CharSequence mTitle;
    FragmentManager fragmentManager;
    @InjectView(R.id.nav_list) ListView mDrawerList;
    @InjectView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    FragmentFactory mFragmentFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mActivityTitle = getTitle().toString();
        loadResources();
        addNavigationDrawerItems();
        setupDrawerToogle();

        fragmentManager = getFragmentManager();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        navMenuIcons.recycle();
        setDrawerListActions();
        // Determines the fragment to be shown when the activity is launched for the first time
        if (savedInstanceState == null) {
            // on first time display view for first nav item
            int homeFragmentPosition = ModelManager.manager.getWelcomeFragmentPosition();
            Fragment fragment = getFragmentView(homeFragmentPosition);
            displayFragmentView(fragment, homeFragmentPosition);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Loads the string resources necessary to populate the navigation drawer
     */
    private void loadResources() {
        navDrawerItems = new ArrayList<NavDrawerItem>();
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
    }

    /**
     * Adds the string resources to the navigation drawer according to the user's access rights
     */
    private void addNavigationDrawerItems() {
        // adding nav drawer items to array based on the order of the xml file
        String accessRights = ModelManager.manager.getCurrentClient().getAccessRights();
        if(accessRights.toLowerCase().equals("guest")) {
            for(int i = 0; i < 2; i++) {
                navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons.getResourceId(i, -1)));
            }
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
        } else {
            for(int i = 0; i < navMenuTitles.length; i++) {
                navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons.getResourceId(i, -1)));
            }
        }
        mAdapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(mAdapter);
    }

    /**
     * Defines the actions to be performed when the navigation drawer changes state (open/closed)
     */
    private void setupDrawerToogle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepateOptionsMenu()
            }
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    /**
     * Determines the actions to be performed when a user clicks on a navigation item
     */
    private void setDrawerListActions() {
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // display view for selected nav drawer item
                Fragment fragment = getFragmentView(position);
                displayFragmentView(fragment, position);
            }
        });
    }

    /**
     * Retrieves the selected fragment from the factory
     * @param position The position of each fragments in the navigation drawer
     * @return
     */
    public Fragment getFragmentView(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment =  new ShowFragment(); //FragmentFactory.getFragment(Fragments.Show);
                break;
            case 1:
                fragment = new TellFragment();
                break;
            case 2:
                if(ModelManager.manager.getCurrentClient().getAccessRights().toLowerCase().equals("guest")){
                    logout();
                    break;
                }
                fragment =  new HomeFragment(); // FragmentFactory.getFragment(Fragments.Home);
                break;
            case 3:
                fragment = FragmentFactory.getFragment(Fragments.Profile); //new ProfileFragment();
                break;
            case 4:
                fragment = FragmentFactory.getFragment(Fragments.Upload);
                break;
            case 5:
                logout();
            case 6:
                fragment = FragmentFactory.getFragment(Fragments.Moderate);
                break;
            default:
                break;
        }
        return fragment;
    }

    /**
     * Logs out and finishes the activity
     */
    private void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    /**
     *
     * @param fragment The fragment to be displayed
     * @param position The position that corresponds the fragments in the navigation drawer
     * @param args Home activity passes the selected photo id as an argument
     */
    public void displayFragmentView(Fragment fragment, int position, Bundle args) {
        fragment.setArguments(args);
        displayFragmentView(fragment, position);
    }

    /**
     *
     * @param fragment
     * @param position
     */
    public void displayFragmentView(Fragment fragment, int position) {
        if (fragment != null) {

            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            fragmentManager.beginTransaction().addToBackStack(null);
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }



}
