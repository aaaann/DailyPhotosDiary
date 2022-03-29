package com.annevonwolffen.settings_impl.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.annevonwolffen.di.FeatureProvider.getInnerFeature
import com.annevonwolffen.mainscreen_api.ToolbarFragment
import com.annevonwolffen.settings_api.SettingsApi
import com.annevonwolffen.settings_impl.R
import com.annevonwolffen.settings_impl.di.SettingsInternalApi
import com.annevonwolffen.ui_utils_api.viewmodel.ViewModelProviderFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class SettingsFragment : PreferenceFragmentCompat() {

    private val settingsInternalApi: SettingsInternalApi by lazy {
        getInnerFeature(
            SettingsApi::class,
            SettingsInternalApi::class
        )
    }

    private val viewModel: SettingsViewModel by activityViewModels {
        ViewModelProviderFactory {
            SettingsViewModel(
                settingsInternalApi.settingsInteractor,
                settingsInternalApi.notificationWorkManager
            )
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureDailyNotificationSetting()

        (parentFragment?.parentFragment as? ToolbarFragment)?.clearToolbarMenu()
    }

    private fun configureDailyNotificationSetting() {
        val notificationSwitchSetting =
            findPreference<SwitchPreferenceCompat>(getString(R.string.daily_notification_key))
                ?.apply {
                    setOnPreferenceChangeListener { _, newValue ->
                        Log.d(TAG, "Включено уведомление: $newValue")
                        if (newValue is Boolean) {
                            viewModel.toggleNotification(newValue)
                        }
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