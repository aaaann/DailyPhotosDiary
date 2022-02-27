package com.annevonwolffen.settings_impl.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.annevonwolffen.settings_impl.domain.SettingsInteractor
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsInteractor: SettingsInteractor) : ViewModel() {

    val isNotificationEnabled: StateFlow<Boolean> = settingsInteractor.isNotificationEnabledFlow
        .catch { t -> Log.w(TAG, "Ошибка при получении значения вкл./выкл. уведомления: $t") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    fun toggleNotification() {
        viewModelScope.launch { settingsInteractor.toggleNotification() }
    }

    private companion object {
        const val TAG = "SettingsViewModel"
    }
}