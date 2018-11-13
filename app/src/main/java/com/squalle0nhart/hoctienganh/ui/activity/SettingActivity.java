package com.squalle0nhart.hoctienganh.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.squalle0nhart.hoctienganh.R;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.MITLicense;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;

/**
 * Created by ThangBK on 27/3/2017.
 */

public class SettingActivity extends AppCompatActivity {
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mContext = this;
        getFragmentManager().beginTransaction()
                .replace(R.id.content, new PrefsFragment()).commit();
    }

    public static class PrefsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_setting);

            Preference pref = findPreference(getString(R.string.key_preference_open_source_license));
            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    final Notices notices = new Notices();
                    notices.addNotice(new Notice("LicensesDialog", "http://psdev.de", "Copyright 2013 Philip Schiffer", new ApacheSoftwareLicense20()));
                    notices.addNotice(new Notice("Android SQLiteAssetHelper", "https://github.com/jgilfelt/android-sqlite-asset-helper", "Copyright (C) 2011 readyState Software Ltd", new ApacheSoftwareLicense20()));
                    notices.addNotice(new Notice("Expandable RecyclerView", "https://github.com/bignerdranch/expandable-recycler-view", "Copyright (c) 2015 Big Nerd Ranch", new MITLicense()));
                    notices.addNotice(new Notice("MaterialProgressBar", "https://github.com/DreaminginCodeZH/MaterialProgressBar", "Copyright 2015 Zhang Hai\n", new ApacheSoftwareLicense20()));
                    new LicensesDialog.Builder(getActivity())
                            .setNotices(notices)
                            .setIncludeOwnLicense(true)
                            .build()
                            .show();
                    return false;
                }
            });
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

        }
    }
}
