package com.annevonwolffen.lintchecks

import com.android.tools.lint.checks.infrastructure.LintDetectorTest.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.annevonwolffen.lintchecks.TestRecyclerViewMissingAttributesDetectorDataProvider.getLayoutFileWithRecyclerViewWithOverScrollModeAlways
import com.annevonwolffen.lintchecks.TestRecyclerViewMissingAttributesDetectorDataProvider.getLayoutFileWithRecyclerViewWithOverScrollModeNever
import com.annevonwolffen.lintchecks.TestRecyclerViewMissingAttributesDetectorDataProvider.getLayoutFileWithRecyclerViewWithoutOverScrollMode
import org.junit.Test

@Suppress("UnstableApiUsage")
class RecyclerViewMissingAttributesDetectorTest {

    @Test
    fun `recycler view without overScrollMode attribute`() {
        lint()
            .allowMissingSdk()
            .files(
                xml(
                    "res/layout/layout.xml",
                    getLayoutFileWithRecyclerViewWithoutOverScrollMode()
                )
            )
            .issues(RecyclerViewMissingAttributesDetector.OVERSCROLL_MODE_NEVER_ISSUE)
            .run()
            .expect(
                """
                res/layout/layout.xml:12: Warning: 
                            Отсутствует атрибут android:overScrollMode со значением never
                         [MissingOverScrollModeNever]
                        <androidx.recyclerview.widget.RecyclerView
                        ^
                0 errors, 1 warnings
            """.trimIndent()
            )
    }

    @Test
    fun `recycler view with incorrect overScrollMode attribute`() {
        lint()
            .allowMissingSdk()
            .files(
                xml(
                    "res/layout/layout.xml",
                    getLayoutFileWithRecyclerViewWithOverScrollModeAlways()
                )
            )
            .issues(RecyclerViewMissingAttributesDetector.OVERSCROLL_MODE_NEVER_ISSUE)
            .run()
            .expect(
                """
                res/layout/layout.xml:19: Warning: 
                            Отсутствует атрибут android:overScrollMode со значением never
                         [MissingOverScrollModeNever]
                            android:overScrollMode="always"
                            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                0 errors, 1 warnings
            """.trimIndent()
            )
    }

    @Test
    fun `recycler view with overScrollMode never`() {
        lint()
            .allowMissingSdk()
            .files(
                xml(
                    "res/layout/layout.xml",
                    getLayoutFileWithRecyclerViewWithOverScrollModeNever()
                )
            )
            .issues(RecyclerViewMissingAttributesDetector.OVERSCROLL_MODE_NEVER_ISSUE)
            .run()
            .expect("No warnings.")
    }
}