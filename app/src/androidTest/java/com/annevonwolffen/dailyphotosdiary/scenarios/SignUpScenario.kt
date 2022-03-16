package com.annevonwolffen.dailyphotosdiary.scenarios

import com.annevonwolffen.dailyphotosdiary.screens.BaseAuthScreen
import com.annevonwolffen.dailyphotosdiary.screens.SignUpScreen
import com.kaspersky.kaspresso.testcases.api.scenario.BaseScenario
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext

class SignUpScenario<ScenarioData> : BaseScenario<ScenarioData>() {
    override val steps: TestContext<ScenarioData>.() -> Unit = {

        step("Открыт экран регистрации") {
            BaseAuthScreen {
                toolbar {
                    isDisplayed()
                    hasTitle("Регистрация")
                }
            }
            SignUpScreen {
                signUpButton { isDisplayed() }
                emailEditText { isDisplayed() }
                passwordEditText { isDisplayed() }
                hasAccountButton { isDisplayed() }
            }
        }
    }
}