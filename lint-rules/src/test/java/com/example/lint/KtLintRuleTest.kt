package com.example.lint

import com.pinterest.ktlint.core.LintError
import com.pinterest.ktlint.test.lint
import org.junit.Test

class KtLintRuleTest {

    @Test
    fun `Given imports are illegal Then throws error`() {
        val errors = KtLintRule().lint(
            """
package com.example.lint

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import kotlin.test.assertEquals

class SomeTest {

    @Mock private lateinit var stringProvider: StringProvider
    @Mock private lateinit var addonCategoryDataHelper: AddonCategoryDataHelper

    @InjectMocks
    private lateinit var effectHandler: DisplayAddonsCategoriesEffectHandler
}
            """.trimIndent()
        )

        assert(
            errors.contains(
                LintError(
                    11,
                    1,
                    KtLintRule.ID,
                    "Illegal imports found in the test class:\n" +
                            "org.mockito.InjectMocks,\n" +
                            "org.mockito.Mock.\n" +
                            "Please use proper test initialization. "
                )
            )
        )
    }
}
