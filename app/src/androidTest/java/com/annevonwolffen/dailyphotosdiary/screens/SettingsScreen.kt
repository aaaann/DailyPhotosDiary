package com.annevonwolffen.dailyphotosdiary.screens

import androidx.test.espresso.DataInteraction
import com.annevonwolffen.settings_impl.presentation.SettingsFragment
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.list.KAbsListView
import io.github.kakaocup.kakao.list.KAdapterItem

object SettingsScreen : KScreen<SettingsScreen>() {
    override val layoutId: Int? = null
    override val viewClass: Class<*>? = SettingsFragment::class.java

    val list = KAbsListView(builder = { withId(androidx.preference.R.id.recycler_view) },
        itemTypeBuilder = { itemType(::SettingItem) })

    class SettingItem(parent: DataInteraction) : KAdapterItem<SettingItem>(parent)

    fun checkSettings(vararg titles: String) {
        list {
            isDisplayed()
        }
        // for (title in titles) {
        //     list.childWith<SettingItem> { } perform { isDisplayed() }
        // }
    }
}