package com.annevonwolffen.detektrules

import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtBinaryExpression
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.psi.KtElement

class ViewModelCreateRule : Rule() {
    override val issue: Issue
        get() = Issue(
            OVERSCROLL_MODE_NEVER_ISSUE_ID,
            Severity.Warning,
            OVERSCROLL_MODE_NEVER_ISSUE_EXPLANATION,
            Debt.FIVE_MINS
        )


    private companion object {
        const val OVERSCROLL_MODE_ATTRIBUTE = "overScrollMode"
        const val OVERSCROLL_MODE_ATTRIBUTE_VALUE = "never"

        const val OVERSCROLL_MODE_NEVER_ISSUE_ID = "MissingOverScrollModeNever"
        const val OVERSCROLL_MODE_NEVER_ISSUE_DESCRIPTION = """
            Отсутствует атрибут `android:overScrollMode` со значением `never`
        """
        const val OVERSCROLL_MODE_NEVER_ISSUE_EXPLANATION = """
            Добавьте android:overScrollMode="never" во избежание появления цветной области 
            под/над списком при прокрутке вниз/вверх.
        """
    }
}