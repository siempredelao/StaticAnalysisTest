package com.example.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UVariable

class AndroidLintDetector: Detector(), Detector.UastScanner {

    companion object {

        val ISSUE = Issue.create(
            "HungarianNotation",
            "Variables named using Hungarian notation",
            "Variables named using Hungarian notation are not allowed.",
            Category.CORRECTNESS,
            6,
            Severity.ERROR,
            Implementation(
                AndroidLintDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }

    override fun getApplicableUastTypes() = listOf(UVariable::class.java)

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        override fun visitVariable(node: UVariable) {
            if (isHungarianNotation(node.name!!)) {
                val message =
                    "${node.name} is named using Hungarian notation, which is not allowed."
                context.report(
                    ISSUE,
                    context.getLocation(node.navigationElement), message
                )
            }
        }
    }

    private fun isHungarianNotation(variable: String): Boolean {
        return variable.length > 2 && variable[0] == 'm' && Character.isUpperCase(variable[1])
    }
}
