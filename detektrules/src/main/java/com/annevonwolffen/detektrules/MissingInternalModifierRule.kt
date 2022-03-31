package com.annevonwolffen.detektrules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtModifierListOwner
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.psiUtil.isPublic

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
        modulePackageName?.getReferencedName()
            // если impl-модуль
            ?.takeIf { it.endsWith("_impl") }
            ?.let {

                val elementsTypes = arrayOf(
                    KtClass::class.java,
                    KtObjectDeclaration::class.java,
                    KtNamedFunction::class.java,
                    KtProperty::class.java
                )

                // пройти по всем элементам
                elementsTypes.forEach {
                    file.findChildrenByClass(it).reportOnEachIfNeeded()
                }
            }
    }

    private fun <T : Any> Array<T>.reportOnEachIfNeeded() {
        filterIsInstance<KtModifierListOwner>()
            .forEach {
                if (it.isPublic) {
                    report(
                        CodeSmell(
                            issue = issue,
                            entity = Entity.from(it),
                            message = "${it.name} $INTERNAL_IMPL_ISSUE_REPORT_MESSAGE"
                        )
                    )
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
