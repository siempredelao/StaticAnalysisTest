package com.example.lint

import com.pinterest.ktlint.core.RuleSet
import com.pinterest.ktlint.core.RuleSetProvider

class KtLintRuleProvider : RuleSetProvider {

    override fun get() = RuleSet(KtLintRule.ID)
}