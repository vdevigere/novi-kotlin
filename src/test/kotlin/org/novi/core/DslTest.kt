package org.novi.core

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat


class DslTest {

    class TrueActivation(override var configuration: String?) : BaseActivation<String>{
        override fun valueOf(configStr: String): BaseActivation<String> = TrueActivation(configStr)

        override fun evaluate(context: String): Boolean = true

    }

    class FalseActivation(override var configuration: String?) : BaseActivation<String>{
        override fun valueOf(configStr: String): BaseActivation<String> = FalseActivation(configStr)

        override fun evaluate(context: String): Boolean = false

    }

    @Test
    fun testEvaluate1(){
        val bEval = FalseActivation("False-1") or (FalseActivation("False-2") and TrueActivation("True-3"))
        assertThat(bEval.evaluate("Hello")).isFalse()
    }

    @Test
    fun testEvaluate2(){
        val bEval = FalseActivation("False-1") and FalseActivation("False-2") or TrueActivation("True-3")
        assertThat(bEval.evaluate("World")).isTrue()
    }

    @Test
    fun testEvaluate3(){
        val bEval = FalseActivation("False-1") and (FalseActivation("False-2") or TrueActivation("True-3"))
        assertThat(bEval.evaluate("World")).isFalse()
    }

    @Test
    fun testEvaluate4(){
        val bEval =  !FalseActivation("False-1") and (FalseActivation("False-2") or TrueActivation("True-3"))
        assertThat(bEval.evaluate("World")).isTrue()
    }
}