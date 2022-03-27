package com.annevonwolffen.dailyphotosdiary.tests

import com.annevonwolffen.authorization_api.di.AuthorizationApi
import com.annevonwolffen.dailyphotosdiary.presentation.StartActivity
import com.annevonwolffen.dailyphotosdiary.scenarios.SignInScenario
import com.annevonwolffen.dailyphotosdiary.scenarios.SignUpScenario
import com.annevonwolffen.dailyphotosdiary.screens.MainScreen
import com.annevonwolffen.dailyphotosdiary.screens.SignInScreen
import com.annevonwolffen.dailyphotosdiary.screens.SignUpScreen
import com.annevonwolffen.dailyphotosdiary.utils.lazyActivityScenarioRule
import com.annevonwolffen.di.FeatureProvider
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class SignInTest : TestCase() {

    @get:Rule
    val rule = lazyActivityScenarioRule<StartActivity>(launchActivity = false)

    @Test
    fun gotoMainScreenFromSignIn() {
        before {
            runBlocking { FeatureProvider.getFeature(AuthorizationApi::class).authInteractor.signOut() }
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
                MainScreen {
                    drawer { isDisplayed() }
                    toolbar {
                        isDisplayed()
                        hasTitle("Дневник")
                    }
                }
            }
        }
    }

    @Test
    fun gotoMainScreenFromSignInStartingWithEmptyLogopass() {
        before {
            runBlocking { FeatureProvider.getFeature(AuthorizationApi::class).authInteractor.signOut() }
            rule.launch()
        }.after {
        }.run {
            step("""Перейти на экран авторизации и ввести email/пароль""") {
                scenario(SignInScenario())
            }
            step("""Стереть логопасс""") {
                SignInScreen {
                    clearEmailAndPassword()
                }
            }
            step("""Нажать кнопку "Войти"""") {
                SignInScreen {
                    signInButton { click() }
                    progressLayout { isNotDisplayed() }
                }
            }
            step("""Заново ввести логопасс""") {
                scenario(SignInScenario())
            }
            step("""Нажать кнопку "Войти"""") {
                SignInScreen {
                    signInButton { click() }
                    progressLayout { isDisplayed() }
                }
                MainScreen {
                    drawer { isDisplayed() }
                    toolbar {
                        isDisplayed()
                        hasTitle("Дневник")
                    }
                }
            }
        }
    }

    @Test
    fun goBackToSignInFromSignUpAndForwardToMain() {
        before {
            runBlocking { FeatureProvider.getFeature(AuthorizationApi::class).authInteractor.signOut() }
            rule.launch()
        }.after {
        }.run {
            step("""Перейти на экран авторизации и ввести email/пароль""") {
                scenario(SignInScenario())
            }
            step("""Нажать на кнопку "Нет аккаунта"""") {
                SignInScreen {
                    noAccountButton { click() }
                }
            }
            step("""Перейти на экран регистрации""") {
                scenario(SignUpScenario())
            }
            step("""Нажать на кнопку "Уже есть аккаунт"""") {
                SignUpScreen {
                    hasAccountButton { click() }
                }
            }
            step("""Нажать кнопку "Войти"""") {
                SignInScreen {
                    signInButton { click() }
                    progressLayout { isDisplayed() }
                }
                MainScreen {
                    drawer { isDisplayed() }
                    toolbar {
                        isDisplayed()
                        hasTitle("Дневник")
                    }
                }
            }
        }
    }
}