package com.wahlzeit.mobile.activities;

import android.app.Fragment;
import android.app.FragmentManager;
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

import com.wahlzeit.mobile.components.navigation.NavDrawerItem;
import com.wahlzeit.mobile.components.navigation.NavDrawerListAdapter;
import com.wahlzeit.mobile.R;
import com.wahlzeit.mobile.fragments.FragmentFactory;
import com.wahlzeit.mobile.fragments.Fragments;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity {

    private NavDrawerListAdapter mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<NavDrawerItem> navDrawerItems;

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private String mActivityTitle;
    // used to store app title
    private CharSequence mTitle;

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        navMenuIcons.recycle();

        setDrawerListActions();

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            int homeFragmentPosition = 2;
            Fragment fragment = getFragmentView(homeFragmentPosition);
            displayFragmentView(fragment, homeFragmentPosition, null);
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

    private void loadResources() {
        navDrawerItems = new ArrayList<NavDrawerItem>();
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
    }

    private void addNavigationDrawerItems() {
        // adding nav drawer items to array based on the order of the xml file
        for(int i = 0; i < navMenuTitles.length; i++) {
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons.getResourceId(i, -1)));
        }
        mAdapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(mAdapter);

    }

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

    private void setDrawerListActions() {
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // display view for selected nav drawer item
                Fragment fragment = getFragmentView(position);
                displayFragmentView(fragment, position, null);
            }
        });
    }

    // replace with an abstract factory in order to make the static
    public Fragment getFragmentView(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = FragmentFactory.getFragment(Fragments.Show); //new ShowFragment();
                break;
            case 1:
                fragment = FragmentFactory.getFragment(Fragments.Tell); //new TellFragment();
                break;
            case 2:
                fragment = FragmentFactory.getFragment(Fragments.Home); //new HomeFragment();
                break;
            case 3:
                fragment = FragmentFactory.getFragment(Fragments.Profile); //new ProfileFragment();
                break;
            case 4:
                fragment = FragmentFactory.getFragment(Fragments.Upload);
                break;
            default:
                break;
        }
        return fragment;
    }

    public void displayFragmentView(Fragment fragment, int position, Bundle args) {
        if (fragment != null) {
            if(args != null) {
                fragment.setArguments(args);
            }
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

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
