package com.annevonwolffen.lintchecks

import com.android.SdkConstants
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LayoutDetector
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Attr
import org.w3c.dom.Element

@Suppress("UnstableApiUsage")
class RecyclerViewMissingAttributesDetector : LayoutDetector() {

    override fun getApplicableElements(): Collection<String> {
        return setOf("androidx.recyclerview.widget.RecyclerView")
    }

    override fun getApplicableAttributes(): Collection<String> {
        return setOf(OVERSCROLL_MODE_ATTRIBUTE)
    }

    override fun visitElement(context: XmlContext, element: Element) {
        if (!element.hasAttributeNS(SdkConstants.ANDROID_URI, OVERSCROLL_MODE_ATTRIBUTE)) {
            context.report(
                OVERSCROLL_MODE_NEVER_ISSUE,
                context.getLocation(element),
                OVERSCROLL_MODE_NEVER_ISSUE_DESCRIPTION,
                fixMissingOverScrollModeNever()
            )
        }
    }

    override fun visitAttribute(context: XmlContext, attribute: Attr) {
        if (attribute.value != OVERSCROLL_MODE_ATTRIBUTE_VALUE) {
            context.report(
                OVERSCROLL_MODE_NEVER_ISSUE,
                context.getLocation(attribute),
                OVERSCROLL_MODE_NEVER_ISSUE_DESCRIPTION,
                fixMissingOverScrollModeNever()
            )
        }
    }

    private fun fixMissingOverScrollModeNever() = LintFix.create()
        .set(SdkConstants.ANDROID_URI, OVERSCROLL_MODE_ATTRIBUTE, OVERSCROLL_MODE_ATTRIBUTE_VALUE)
        .build()

    companion object {
        private const val OVERSCROLL_MODE_ATTRIBUTE = "overScrollMode"
        private const val OVERSCROLL_MODE_ATTRIBUTE_VALUE = "never"

        private const val OVERSCROLL_MODE_NEVER_ISSUE_ID = "MissingOverScrollModeNever"
        private const val OVERSCROLL_MODE_NEVER_ISSUE_DESCRIPTION = """
            Отсутствует атрибут `android:overScrollMode` со значением `never`
        """
        private const val OVERSCROLL_MODE_NEVER_ISSUE_EXPLANATION = """
            Добавьте android:overScrollMode="never" во избежание появления цветной области 
            под/над списком при прокрутке вниз/вверх.
        """

        val OVERSCROLL_MODE_NEVER_ISSUE = Issue.create(
            OVERSCROLL_MODE_NEVER_ISSUE_ID,
            OVERSCROLL_MODE_NEVER_ISSUE_DESCRIPTION,
            OVERSCROLL_MODE_NEVER_ISSUE_EXPLANATION,
            Category.COMPLIANCE,
            5,
            Severity.WARNING,
            Implementation(
                RecyclerViewMissingAttributesDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE
            )
        )
    }
}