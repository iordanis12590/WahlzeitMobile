package com.wahlzeit.mobile.fragments;

import android.app.Fragment;

/**
 *  A factory that hides the implementation of the fragments' implementation
 */
public class FragmentFactory {

    static private Fragment showFragment;
    static private Fragment tellFragment;
    static private Fragment homeFragment;
    static private Fragment profileFragment;
    static private Fragment uploadFragment;
    static private Fragment moderateFragment;

    public static Fragment getFragment(Fragments fragment) {
        switch (fragment) {
            case Show: return showFragment == null ? showFragment = new ShowFragment() : showFragment;
            case Tell: return tellFragment == null ? tellFragment = new TellFragment() : tellFragment;
            case Home: return homeFragment == null ? homeFragment = new HomeFragment() : homeFragment;
            case Profile: return profileFragment == null ? profileFragment = new ProfileFragment() : profileFragment;
            case Upload: return uploadFragment == null ? uploadFragment = new UploadFragment() : uploadFragment;
            case Moderate: return moderateFragment == null ? moderateFragment = new ModerateFragment() : moderateFragment;
        }
        return null;
    }

}
