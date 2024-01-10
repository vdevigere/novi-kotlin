package org.novi.activations.factories

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.novi.NoviConfiguration
import org.novi.core.AndActivationFactory
import org.novi.persistence.ActivationConfig
import org.novi.persistence.ActivationConfigRepository
import java.util.*

class NoviOperationActivationConfigAwareTest {

    @Test
    fun testAnd() {
        val op1config = ActivationConfig(
            config = "{\"startDateTime\":\"11-12-2023 12:00\",\"endDateTime\":\"20-12-2023 12:00\" }",
            description = "Date Time",
            name = "org.novi.activations.DateTimeActivationFactory",
            id = 1L
        )
        val op2config = ActivationConfig(
            config = "{\"SampleA\":100.0,\"SampleB\":0,\"SampleC\":0}",
            description = "Always Sample A",
            name = "org.novi.activations.WeightedRandomActivationFactory",
            id = 2L
        )

        val mockRepo = mock(ActivationConfigRepository::class.java)
        `when`(mockRepo.findById(1L)).thenReturn(Optional.of(op1config))
        `when`(mockRepo.findById(2L)).thenReturn(Optional.of(op2config))

        val context = """
                {
                    "org.novi.activations.DateTimeActivation.currentDateTime": "15-12-2023 12:00",
                    "org.novi.activations.WeightedRandomActivation":{
                                            "seed": 200,
                                            "variantToCheck": "SampleB"
                                        }                    
                }
                """
        val config = """
            {"activationIds":[1,2],"operation":"AND"}
        """.trimIndent()
        val factory = NoviOperationActivationFactory()
        val activation = factory.setActivationConfigRepository(mockRepo).setConfiguration(config)
        assertThat(activation.evaluate(context)).isFalse
    }

    @Test
    fun testOr() {
        val op1config = ActivationConfig(
            config = "{\"startDateTime\":\"11-12-2023 12:00\",\"endDateTime\":\"20-12-2023 12:00\" }",
            description = "Date Time",
            name = "org.novi.activations.DateTimeActivationFactory",
            id = 1L
        )
        val op2config = ActivationConfig(
            config = "{\"SampleA\":100.0,\"SampleB\":0,\"SampleC\":0}",
            description = "Always Sample A",
            name = "org.novi.activations.WeightedRandomActivationFactory",
            id = 2L
        )

        val mockRepo = mock(ActivationConfigRepository::class.java)
        `when`(mockRepo.findById(1L)).thenReturn(Optional.of(op1config))
        `when`(mockRepo.findById(2L)).thenReturn(Optional.of(op2config))

        val context = """
                {
                    "org.novi.activations.DateTimeActivation.currentDateTime": "15-12-2023 12:00",
                    "org.novi.activations.WeightedRandomActivation":{
                                            "seed": 200,
                                            "variantToCheck": "SampleA"
                                        }                    
                }
                """
        val config = """
            {"activationIds":[1,2],"operation":"OR"}
        """.trimIndent()
        val factory = NoviOperationActivationFactory()
        val activation = factory.setActivationConfigRepository(mockRepo).setConfiguration(config)
        assertThat(activation.evaluate(context)).isTrue
    }

    @Test
    fun testNot() {
        val op1config = ActivationConfig(
            config = "{\"startDateTime\":\"11-12-2023 12:00\",\"endDateTime\":\"20-12-2023 12:00\" }",
            description = "Date Time",
            name = "org.novi.activations.DateTimeActivationFactory",
            id = 1L
        )

        val mockRepo = mock(ActivationConfigRepository::class.java)
        `when`(mockRepo.findById(1L)).thenReturn(Optional.of(op1config))

        val context = """
                {
                    "org.novi.activations.DateTimeActivation.currentDateTime": "15-12-2023 12:00"             
                }
                """
        val config = """
            {"activationIds":[1],"operation":"NOT"}
        """.trimIndent()
        val factory = NoviOperationActivationFactory()
        val activation = factory.setActivationConfigRepository(mockRepo).setConfiguration(config)
        assertThat(activation.evaluate(context)).isFalse
    }

    @Test
    fun testAndActivationFactory() {
        val op1config = ActivationConfig(
            config = "{\"startDateTime\":\"11-12-2023 12:00\",\"endDateTime\":\"20-12-2023 12:00\" }",
            description = "Date Time",
            name = "org.novi.activations.DateTimeActivationFactory",
            id = 1L
        )
        val op2config = ActivationConfig(
            config = "{\"SampleA\":100.0,\"SampleB\":0,\"SampleC\":0}",
            description = "Always Sample A",
            name = "org.novi.activations.WeightedRandomActivationFactory",
            id = 2L
        )

        val mockRepo = mock(ActivationConfigRepository::class.java)
        `when`(mockRepo.findById(1L)).thenReturn(Optional.of(op1config))
        `when`(mockRepo.findById(2L)).thenReturn(Optional.of(op2config))
        `when`(mockRepo.findAllById(listOf(1L, 2L))).thenReturn(listOf(op1config, op2config))
        val context = """
                {
                    "org.novi.activations.DateTimeActivation.currentDateTime": "15-12-2023 12:00",
                    "org.novi.activations.WeightedRandomActivation":{
                                            "seed": 200,
                                            "variantToCheck": "SampleB"
                                        }                    
                }
                """
        val factory = AndActivationFactory()
        val activation = factory.setActivationConfigRepository(mockRepo).setConfiguration("[1,2]")
        assertThat(activation.evaluate(context)).isFalse
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun initializeRegistry(): Unit {
            NoviConfiguration()
        }
    }
}