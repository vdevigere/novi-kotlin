package org.novi.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.novi.activations.DateTimeActivation


class DslTest {

    @Test
    fun testEvaluate2() {
        val bEval =
            FalseActivation().setConfiguration(configuration = "False-1") and FalseActivation().setConfiguration(configuration = "False-2") or TrueActivation().setConfiguration(
                configuration = "True-3"
            )
        assertThat(bEval.evaluate("World")).isTrue()
    }

    @Test
    fun testEvaluate3() {
        val bEval =
            FalseActivation().setConfiguration(configuration = "False-1") and (FalseActivation().setConfiguration(
                configuration = "False-2"
            ) or TrueActivation().setConfiguration(
                configuration = "True-3"
            ))
        assertThat(bEval.evaluate("World")).isFalse()
    }

    @Test
    fun testEvaluate4() {
        val bEval =
            !FalseActivation().setConfiguration(configuration = "False-1") and (FalseActivation().setConfiguration(
                configuration = "False-2"
            ) or TrueActivation().setConfiguration(
                configuration = "True-3"
            ))
        assertThat(bEval.evaluate("World")).isTrue()
    }

    @Test
    fun evaluateForDateEqStartDate() {
        val config = """
                {
                    "startDateTime":"11-12-2023 12:00",
                    "endDateTime":"20-12-2023 12:00"
                }
                """
        val context = """
                {
                    "org.novi.activations.DateTimeActivation.currentDateTime": "15-12-2023 12:00"
                }
                """
        val bEval =
            DateTimeActivation().setConfiguration(configuration = config) and TrueActivation().setConfiguration(configuration = "True-1")
        assertThat(bEval.evaluate(context)).isTrue
    }

    @Test
    fun evaluateForDateEqStartDateAltFormat() {
        val configAltformat = """
                {
                    "startDateTime":"12/11/2023 12:00",
                    "endDateTime":"12/20/2023 12:00"
                }
        """.trimIndent()
        val contextAltFormat = """
                {
                    "org.novi.activations.DateTimeActivation.currentDateTime": "12/15/2023 12:00"
                }
                """
        val bEval2 =
            DateTimeActivation(dateFormat = "MM/dd/yyyy hh:mm").setConfiguration(configuration = configAltformat) and FalseActivation().setConfiguration(
                configuration = "False-1"
            )
        assertThat(bEval2.evaluate(contextAltFormat)).isFalse
    }
}