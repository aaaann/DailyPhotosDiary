package com.annevonwolffen.detektrules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.com.intellij.lang.jvm.JvmModifier
import org.jetbrains.kotlin.psi.KtFile

class MissingInternalModifierRule : Rule() {
    override val issue: Issue
        get() = Issue(
            INTERNAL_IMPL_ISSUE_ID,
            Severity.Warning,
            INTERNAL_IMPL_ISSUE_DESCRIPTION,
            Debt.FIVE_MINS
        )

    override fun visitKtFile(file: KtFile) {
        super.visitKtFile(file)
        val modulePackageName = file.packageDirective?.packageNames?.get(2)
        modulePackageName?.name
            // если impl-модуль
            ?.takeIf { it.endsWith("_impl") }
            ?.let {
                // пройти по всем классам
                file.classes.forEach {
                    if (it.hasModifier(JvmModifier.PUBLIC)) {
                        report(
                            CodeSmell(
                                issue = issue,
                                entity = Entity.from(it),
                                message = "$it $INTERNAL_IMPL_ISSUE_REPORT_MESSAGE"
                            )
                        )
                    }
                }
            }
    }

    private companion object {

        const val INTERNAL_IMPL_ISSUE_ID = "MissingInternalModifier"
        const val INTERNAL_IMPL_ISSUE_DESCRIPTION = """
            Сущности внутри impl-модуля должны иметь модификатор доступа `internal`.
        """
        const val INTERNAL_IMPL_ISSUE_REPORT_MESSAGE = """
            должен быть помечен модификатором доступа `internal`, так как находится внутри impl-модуля 
            и не может быть виден извне.
        """
    }
}