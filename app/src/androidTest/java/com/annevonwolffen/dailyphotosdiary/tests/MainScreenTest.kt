package com.annevonwolffen.dailyphotosdiary.tests

import com.annevonwolffen.authorization_api.di.AuthorizationApi
import com.annevonwolffen.dailyphotosdiary.presentation.StartActivity
import com.annevonwolffen.dailyphotosdiary.scenarios.SignInScenario
import com.annevonwolffen.dailyphotosdiary.screens.GalleryScreen
import com.annevonwolffen.dailyphotosdiary.screens.MainScreen
import com.annevonwolffen.dailyphotosdiary.screens.SettingsScreen
import com.annevonwolffen.dailyphotosdiary.screens.SignInScreen
import com.annevonwolffen.dailyphotosdiary.utils.lazyActivityScenarioRule
import com.annevonwolffen.di.FeatureProvider
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class MainScreenTest : TestCase() {

    @get:Rule
    val rule = lazyActivityScenarioRule<StartActivity>(launchActivity = false)

    @Test
    fun onGalleryScreen() {
        before {
            FeatureProvider.getFeature(AuthorizationApi::class.java).authInteractor.signOut()
            rule.launch()
        }.after {
        }.run {
            step("""Перейти на экран авторизации и ввести email/пароль""") {
                scenario(SignInScenario())
            }
            step("""Нажать кнопку "Войти"""") {
                SignInScreen {
                    signInButton { click() }
                    progressLayout { isDisplayed() }
                }
            }
            step("""На экране Дневника""") {
                MainScreen {
                    drawer { isDisplayed() }
                    toolbar {
                        isDisplayed()
                        hasTitle("Дневник")
                    }
                }
                GalleryScreen {
                    imagesGroupRecycler {
                        isDisplayed()
                    }
                    addButton { isDisplayed() }
                }
            }
        }
    }

    @Test
    fun gotoSettingsScreen() {
        before {
            FeatureProvider.getFeature(AuthorizationApi::class.java).authInteractor.signOut()
            rule.launch()
        }.after {
        }.run {
            step("""Перейти на экран авторизации и ввести email/пароль""") {
                scenario(SignInScenario())
            }
            step("""Нажать кнопку "Войти"""") {
                SignInScreen {
                    signInButton { click() }
                    progressLayout { isDisplayed() }
                }
            }
            step("""На экране Дневника""") {
                MainScreen {
                    drawer { isDisplayed() }
                    toolbar {
                        isDisplayed()
                        hasTitle("Дневник")
                    }
                }
            }
            step("""В меню выбрать Настройки""") {
                MainScreen {
                    openSettings()
                    toolbar {
                        isDisplayed()
                        hasTitle("Настройки")
                    }
                }
                SettingsScreen {
                    checkSettings("Ежедневное напоминание")
                }
            }
        }
    }
}