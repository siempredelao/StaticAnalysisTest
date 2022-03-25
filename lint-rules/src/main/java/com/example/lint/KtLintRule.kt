package com.example.lint

import com.pinterest.ktlint.core.Rule
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.stubs.elements.KtStubElementTypes
import org.jetbrains.kotlin.utils.addToStdlib.ifNotEmpty

class KtLintRule : Rule(ID) {

    private var visitingTestClass = false
    private var importCheckPassed = false
    private val imports: MutableList<ASTNode> = mutableListOf()

    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        if (shouldResetFlags(node)) {
            imports.clear()
            visitingTestClass = false
            importCheckPassed = false
        }
        addImportsForImportNode(node)

        checkIfVisitingTest(node)
        if (visitingTestClass) {
            checkIllegalImports(emit)
        }
    }

    private fun checkIllegalImports(emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit) {
        if (importCheckPassed) return

        imports
            .filter { node ->
                val importValue = node.text.replace("import ", "")
                ILLEGAL_IMPORTS.contains(importValue)
            }
            .ifNotEmpty {
                val illegalImportsText = joinToString(",\n") { node ->
                    node.text.replace("import ", "")
                }
                val illegalImportsErrorMessage =
                    "Illegal imports found in the test class:\n$illegalImportsText.\n" +
                            "Please use proper test initialization. "
                emit.invoke(last().startOffset, illegalImportsErrorMessage, false)
                importCheckPassed = true
            }
    }

    private fun addImportsForImportNode(node: ASTNode) {
        if (node.elementType == KtStubElementTypes.IMPORT_DIRECTIVE) {
            imports.add(node)
        }
    }

    private fun shouldResetFlags(node: ASTNode): Boolean {
        return node.elementType == KtStubElementTypes.PACKAGE_DIRECTIVE
    }

    private fun checkIfVisitingTest(node: ASTNode) {
        val isParentOfTypeClass = node.treeParent?.elementType == KtStubElementTypes.CLASS
        val isNodeOfTypeIdentifier = node.elementType == KtTokens.IDENTIFIER
        if (node.text.contains("Test") && isParentOfTypeClass && isNodeOfTypeIdentifier) {
            visitingTestClass = true
        }
    }

    companion object {

        private val ILLEGAL_IMPORTS = arrayOf("org.mockito.InjectMocks", "org.mockito.Mock")

        const val ID = "unit-test-initialization"
    }
}
