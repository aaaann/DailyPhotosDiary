package com.annevonwolffen.settings_impl.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.annevonwolffen.di.FeatureProvider.getFeature
import com.annevonwolffen.settings_impl.R
import com.annevonwolffen.settings_impl.di.SettingsInternalApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SettingsViewModel(getFeature(SettingsInternalApi::class.java).settingsInteractor) as T
            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureDailyNotificationSetting()
    }

    private fun configureDailyNotificationSetting() {
        val notificationSwitchSetting =
            findPreference<SwitchPreferenceCompat>(getString(R.string.daily_notification_key))
                ?.apply {
                    setOnPreferenceChangeListener { _, newValue ->
                        Log.d(TAG, "Включено уведомление: $newValue")
                        viewModel.toggleNotification()
                        true
                    }
                }

        viewModel.isNotificationEnabled
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { notificationSwitchSetting?.isChecked = it }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private companion object {
        const val TAG = "SettingsFragment"
    }
}