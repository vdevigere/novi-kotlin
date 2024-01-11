package org.novi.core

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotSame
import org.junit.jupiter.api.Test
import org.novi.activations.*
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
        val loader = ServiceLoader.load(ActivationConfigAware::class.java)
        assertThat(loader).hasAtLeastOneElementOfType(DateTimeActivationFactory::class.java)
        assertThat(loader).hasAtLeastOneElementOfType(WeightedRandomActivationFactory::class.java)
        for (factory in loader) {
            when (factory) {
                is DateTimeActivationFactory -> {
                    val dta: DateTimeActivation = factory.setConfiguration(dtaConfig) as DateTimeActivation
                    val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm")
                    val mapper = jacksonObjectMapper().setDateFormat(sdf)
                    val drd = mapper.readValue<DateRangeData>(dtaConfig)
                    assertThat(dta.parsedConfig).isEqualTo(drd)
                }

                is WeightedRandomActivationFactory -> {
                    val newInstance = factory.setConfiguration(wrConfig.trimIndent())
                    assertThat(newInstance.parsedConfig).isEqualTo(newInstance.valueOf(wrConfig))
                }
            }

        }
    }

    @Test
    fun testCloneServiceLoader() {
        val loader = ServiceLoader.load(ActivationConfigAware::class.java)
        for (factory in loader) {
            when (factory) {
                //Using a factory returns a new Instance
                is WeightedRandomActivationFactory -> {
                    val clone = factory.setConfiguration(wrConfig)
                    assertNotSame(clone, factory)
                }
            }
        }
    }
}