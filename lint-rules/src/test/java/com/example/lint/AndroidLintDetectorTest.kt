package com.example.lint

import com.android.tools.lint.checks.infrastructure.TestFiles
import com.android.tools.lint.checks.infrastructure.TestLintTask
import com.android.tools.lint.checks.infrastructure.TestLintTask.*
import org.junit.Test

class AndroidLintDetectorTest {

    @Test
    fun `Given a field prefixed with m Then returns an error`() {
        val mHello = TestFiles.java(
            """
      package foo;
      public class Hello {
        private String mHello;
      }"""
        ).indented()

        val run = lint()
            .allowMissingSdk()
            .files(mHello)
            .issues(AndroidLintDetector.ISSUE)
            .run()

        run.expectErrorCount(1)
    }
}