package org.novi.core

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.novi.activations.DateRangeData
import org.novi.activations.DateTimeActivation
import org.novi.activations.DateTimeActivationFactory
import org.novi.activations.WeightedRandomActivationFactory
import java.text.SimpleDateFormat
import java.util.*


class ServiceLoaderTestWithId {
    companion object {
        private const val dtaConfig = """
                {
                    "startDateTime":"11-12-2023 12:00",
                    "endDateTime":"20-12-2023 12:00"
                }
            """
        private const val wrConfig = """
                {
                "SampleA":100.0,
                "SampleB":0,
                "SampleC":0
                }
                """
    }

    @Test
    fun testServiceLoaderFactory() {
        val loader = ServiceLoader.load(ActivationFactory::class.java)
        Assertions.assertThat(loader).hasAtLeastOneElementOfType(DateTimeActivationFactory::class.java)
        Assertions.assertThat(loader).hasAtLeastOneElementOfType(WeightedRandomActivationFactory::class.java)
        for (factory in loader) {
            when (factory) {
                is DateTimeActivationFactory -> {
                    val dta: DateTimeActivation = factory.withConfiguration(dtaConfig) as DateTimeActivation
                    val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm")
                    val mapper = jacksonObjectMapper().setDateFormat(sdf)
                    val drd = mapper.readValue<DateRangeData>(dtaConfig)
                    Assertions.assertThat(dta.parsedConfig).isEqualTo(drd)
                }

                is WeightedRandomActivationFactory -> {
//                    Assertions.assertThat(activation.configuration).isNull()
                    val newInstance = factory.withConfiguration(wrConfig.trimIndent())
                    Assertions.assertThat(newInstance.parsedConfig).isEqualTo(newInstance.valueOf(wrConfig))
                }
            }

        }
    }
}