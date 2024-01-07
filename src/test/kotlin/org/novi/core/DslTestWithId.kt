package org.novi.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.novi.activations.DateTimeActivationWithId
import org.novi.activations.WeightedRandomActivationWithId
import org.novi.persistence.ActivationConfig
import org.novi.persistence.ActivationConfigRepository
import java.util.*


class DslTestWithId {

    private val mockRepo: ActivationConfigRepository = mock(ActivationConfigRepository::class.java)

    @BeforeEach
    fun init() {
        val false1 = mock(ActivationConfig::class.java)
        `when`(false1.config).thenReturn("False-1")
        `when`(mockRepo.findById(1L)).thenReturn(Optional.of(false1))

        val false2 = mock(ActivationConfig::class.java)
        `when`(false2.config).thenReturn("False-2")
        `when`(mockRepo.findById(2L)).thenReturn(Optional.of(false2))

        val true3 = mock(ActivationConfig::class.java)
        `when`(true3.config).thenReturn("True-3")
        `when`(mockRepo.findById(3L)).thenReturn(Optional.of(true3))

        val dateActivation = mock(ActivationConfig::class.java)
        `when`(dateActivation.config).thenReturn(
            """
                {
                    "startDateTime":"11-12-2023 12:00",
                    "endDateTime":"20-12-2023 12:00"
                }
                """
        )
        `when`(mockRepo.findById(4L)).thenReturn(Optional.of(dateActivation))

        val wrActivation = mock(ActivationConfig::class.java)
        `when`(wrActivation.config).thenReturn(
            """
                {
                "SampleA":100.0,
                "SampleB":0,
                "SampleC":0
                }
                """
        )
        `when`(mockRepo.findById(5L)).thenReturn(Optional.of(wrActivation))

        val dateActivationAlt = mock(ActivationConfig::class.java)
        `when`(dateActivationAlt.config).thenReturn(
            """
                {
                    "startDateTime":"12/11/2023 12:00",
                    "endDateTime":"12/20/2023 12:00"
                }
                """
        )
        `when`(mockRepo.findById(6L)).thenReturn(Optional.of(dateActivationAlt))
    }

    @Test
    fun testEvaluate1() {
        val bEval = FalseActivation(1L) or (FalseActivation(2L) and TrueActivation(3L))
        assertThat(bEval.setActivationConfigRepository(mockRepo).evaluate("Hello")).isFalse()
    }

    @Test
    fun testEvaluate2() {
        val bEval = FalseActivation(1L) and FalseActivation(2L) or TrueActivation(3L)
        assertThat(bEval.setActivationConfigRepository(mockRepo).evaluate("World")).isTrue()
    }

    @Test
    fun testEvaluate3() {
        val bEval = FalseActivation(1L) and (FalseActivation(2L) or TrueActivation(3L))
        assertThat(bEval.setActivationConfigRepository(mockRepo).evaluate("World")).isFalse()
    }

    @Test
    fun testEvaluate4() {
        val bEval = !FalseActivation(1L) and (FalseActivation(2L) or TrueActivation(3L))
        assertThat(bEval.setActivationConfigRepository(mockRepo).evaluate("World")).isTrue()
    }

    @Test
    fun evaluateForDateEqStartDate() {
        val context = """
                {
                    "org.novi.activations.DateTimeActivationWithId.currentDateTime": "15-12-2023 12:00",
                    "org.novi.activations.WeightedRandomActivationWithId":{
                                            "seed": 200,
                                            "variantToCheck": "SampleA"
                                        }                    
                }
                """
        val bEval = DateTimeActivationWithId(4L) and WeightedRandomActivationWithId(5L)
        assertThat(bEval.setActivationConfigRepository(mockRepo).evaluate(context)).isTrue
    }

    @Test
    fun evaluateForDateEqStartDateAltFormat() {
        val contextAltFormat = """
                {
                    "org.novi.activations.DateTimeActivationWithId.currentDateTime": "12/15/2023 12:00",
                    "org.novi.activations.WeightedRandomActivationWithId":{
                                                                "seed": 200,
                                                                "variantToCheck": "SampleB"
                                                            }                    
                }
                """
        val bEval2 =
            DateTimeActivationWithId(6L, dateFormat = "MM/dd/yyyy hh:mm") and WeightedRandomActivationWithId(5L)
        assertThat(bEval2.setActivationConfigRepository(mockRepo).evaluate(contextAltFormat)).isFalse
    }
}