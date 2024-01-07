package org.novi.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.novi.persistence.BaseActivationWithId
import java.util.*
import kotlin.reflect.KClass


class DslTest {
    private val REGISTRY = HashMap<KClass<out BaseActivationWithId<*>>, BaseActivationWithId<*>>()

    init {
        val loader: ServiceLoader<BaseActivationWithId<*>> = ServiceLoader.load(BaseActivationWithId::class.java)
        for (activation in loader) {
            REGISTRY[activation::class] = activation
        }
    }


    @Test
    fun testEvaluate1() {
        val false1 = REGISTRY[FalseActivation::class]
        false1!!.withConfiguration("False-1")

        val false2 = REGISTRY[FalseActivation::class]
        false2!!.withConfiguration("False-2")

        val true1 = REGISTRY[TrueActivation::class]
        true1!!.withConfiguration( "True-1")
        val bEval = false1 or (false2 and true1)
        assertThat(bEval.evaluate("Hello")).isFalse()
    }
//
//    @Test
//    fun testEvaluate2() {
//        val bEval = FalseActivation("False-1") and FalseActivation("False-2") or TrueActivation("True-3")
//        assertThat(bEval.evaluate("World")).isTrue()
//    }
//
//    @Test
//    fun testEvaluate3() {
//        val bEval = FalseActivation("False-1") and (FalseActivation("False-2") or TrueActivation("True-3"))
//        assertThat(bEval.evaluate("World")).isFalse()
//    }
//
//    @Test
//    fun testEvaluate4() {
//        val bEval = !FalseActivation("False-1") and (FalseActivation("False-2") or TrueActivation("True-3"))
//        assertThat(bEval.evaluate("World")).isTrue()
//    }
//
//    @Test
//    fun evaluateForDateEqStartDate() {
//        val config = """
//                {
//                    "startDateTime":"11-12-2023 12:00",
//                    "endDateTime":"20-12-2023 12:00"
//                }
//                """
//        val context = """
//                {
//                    "org.novi.activations.DateTimeActivation.currentDateTime": "15-12-2023 12:00"
//                }
//                """
//        val bEval = DateTimeActivation(config) and TrueActivation("True-1")
//        assertThat(bEval.evaluate(context)).isTrue
//    }
//
//    @Test
//    fun evaluateForDateEqStartDateAltFormat() {
//        val configAltformat = """
//                {
//                    "startDateTime":"12/11/2023 12:00",
//                    "endDateTime":"12/20/2023 12:00"
//                }
//        """.trimIndent()
//        val contextAltFormat = """
//                {
//                    "org.novi.activations.DateTimeActivation.currentDateTime": "12/15/2023 12:00"
//                }
//                """
//        val bEval2 = DateTimeActivation(configAltformat, "MM/dd/yyyy hh:mm") and FalseActivation("False-1")
//        assertThat(bEval2.evaluate(contextAltFormat)).isFalse
//    }
}