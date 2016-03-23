package com.wahlzeit.mobile.fragments;

import android.app.Fragment;

import com.wahlzeit.mobile.fragments.home.HomeFragment;
import com.wahlzeit.mobile.fragments.profile.ProfileFragment;
import com.wahlzeit.mobile.fragments.show.ShowFragment;

public class FragmentFactory {

    static private Fragment showFragment;
    static private Fragment tellFragment;
    static private Fragment homeFragment;
    static private Fragment profileFragment;
    static private Fragment uploadFragment;
    static private Fragment moderateFragment;
    static private Fragment administerFragment;

    public static Fragment getFragment(Fragments fragment) {

        switch (fragment) {
            case Show: return showFragment == null ? new ShowFragment() : showFragment;
            case Tell: return tellFragment == null ? new TellFragment() : tellFragment;
            case Home: return homeFragment == null ? new HomeFragment() : homeFragment;
            case Profile: return profileFragment == null ? new ProfileFragment() : profileFragment;
            case Upload: return uploadFragment == null ? new UploadFragment() : uploadFragment;
            case Moderate: return moderateFragment == null ? new ModerateFragment() : moderateFragment;
            case Administer: return administerFragment == null ? new AdministerFragment() : administerFragment; //getAdministerFragment();
        }

        return null;
    }
    
}
