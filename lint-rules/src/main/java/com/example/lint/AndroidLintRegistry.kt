package com.example.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.Issue

class AndroidLintRegistry: IssueRegistry() {
    override val issues: List<Issue> = listOf(AndroidLintDetector.ISSUE)
}