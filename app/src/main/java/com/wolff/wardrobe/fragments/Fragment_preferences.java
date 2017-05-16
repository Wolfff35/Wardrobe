package com.wolff.wardrobe.fragments;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;


import com.wolff.wardrobe.R;

/**
 * Created by wolff on 24.04.2017.
 */

public class Fragment_preferences extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preference_general);

    }
}
