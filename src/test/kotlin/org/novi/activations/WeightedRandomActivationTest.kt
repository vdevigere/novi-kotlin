package org.novi.activations

import org.apache.commons.math3.util.Pair
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WeightedRandomActivationTest {
    @Test
    fun parseConfiguration() {
        val config =
            """
                {
                "SampleA":50.0,
                "SampleB":25.0,
                "SampleC":25.0
                }
                """
        val wra = WeightedRandomActivation(config)
        assertThat(wra.parsedConfig).contains(
            Pair.create("SampleA", 50.0),
            Pair.create("SampleB", 25.0),
            Pair.create("SampleC", 25.0)
        )
    }

    @Test
    fun testEvaluateToTrue() {
        val config =
            """
                {
                "SampleA":100.0,
                "SampleB":0,
                "SampleC":0
                }
                """
        val context =
            """
                {
                    "org.novi.activations.WeightedRandomActivation":{
                        "seed": 200,
                        "variantToCheck": "SampleA"
                    }
                }
                """
        val wra = WeightedRandomActivation(config)
        assertThat(wra.evaluate(context)).isTrue
    }

    @Test
    fun testEvaluateToFalse() {
        val config =
            """
                {
                "SampleA":0.0,
                "SampleB":100,
                "SampleC":0
                }
                """
        val context =
            """
                {
                    "org.novi.activations.WeightedRandomActivation":{
                        "seed": 200,
                        "variantToCheck": "SampleA"
                    }
                }
                """
        val wra = WeightedRandomActivation(config)
        assertThat(wra.evaluate(context)).isFalse
    }
}