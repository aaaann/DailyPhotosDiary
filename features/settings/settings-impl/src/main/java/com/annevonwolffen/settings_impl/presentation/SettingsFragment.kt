package com.annevonwolffen.settings_impl.presentation

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.annevonwolffen.settings_impl.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}