package com.annevonwolffen.settings_impl.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annevonwolffen.settings_impl.domain.SettingsInteractor
import com.annevonwolffen.settings_impl.domain.notification.NotificationWorkManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val notificationWorkManager: NotificationWorkManager
) : ViewModel() {

    val isNotificationEnabled: StateFlow<Boolean> = settingsInteractor.isNotificationEnabledFlow
        .catch { t -> Log.w(TAG, "Ошибка при получении значения вкл./выкл. уведомления: $t") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    fun toggleNotification(isNotificationOn: Boolean) {
        viewModelScope.launch { settingsInteractor.toggleNotification() }
        if (isNotificationOn) {
            notificationWorkManager.scheduleNotification()
        } else {
            notificationWorkManager.cancelNotification()
        }
    }

    private companion object {
        const val TAG = "SettingsViewModel"
    }
}