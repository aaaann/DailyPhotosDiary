package com.annevonwolffen.lintchecks

import com.android.tools.lint.checks.infrastructure.LintDetectorTest.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.annevonwolffen.lintchecks.TestRecyclerViewMissingAttributesDetectorDataProvider.getLayoutFileWithRecyclerViewWithOverScrollModeNever
import org.junit.Test

@Suppress("UnstableApiUsage")
class RecyclerViewMissingAttributesDetectorTest {

    @Test
    fun `recycler view without overScrollMode attribute`() {
    }

    @Test
    fun `recycler view with incorrect overScrollMode attribute`() {
    }

    @Test
    fun `recycler view with overScrollMode never`() {
        lint()
            .allowMissingSdk()
            .files(xml("res/layout/layout.xml", getLayoutFileWithRecyclerViewWithOverScrollModeNever()))
            .issues(RecyclerViewMissingAttributesDetector.OVERSCROLL_MODE_NEVER_ISSUE)
            .run()
            .expect("No warnings.")
    }
}