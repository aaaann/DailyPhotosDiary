package com.annevonwolffen.dailyphotosdiary.scenarios

import com.annevonwolffen.dailyphotosdiary.screens.BaseAuthScreen
import com.annevonwolffen.dailyphotosdiary.screens.SignInScreen
import com.kaspersky.kaspresso.testcases.api.scenario.BaseScenario
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext

class SignInScenario<ScenarioData> : BaseScenario<ScenarioData>() {
    override val steps: TestContext<ScenarioData>.() -> Unit = {

        step("Открыт экран авторизации") {
            BaseAuthScreen {
                toolbar {
                    isDisplayed()
                    hasTitle("Вход")
                }
            }
            SignInScreen {
                signInButton { isDisplayed() }
                emailEditText { isDisplayed() }
                passwordEditText { isDisplayed() }
                noAccountButton { isDisplayed() }
            }
        }

        step("""Ввести email/пароль""") {
            SignInScreen {
                enterEmailAndPassword(
                    "aaterekhova_1@edu.hse.ru",
                    "diegelbeaktuell153"
                )
            }
        }
    }
}