package com.annevonwolffen.lintchecks

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue

@Suppress("UnstableApiUsage")
class CustomLintRegistry : IssueRegistry() {
    override val api: Int = com.android.tools.lint.detector.api.CURRENT_API

    override val issues: List<Issue>
        get() = listOf(RecyclerViewMissingAttributesDetector.OVERSCROLL_MODE_NEVER_ISSUE)
}