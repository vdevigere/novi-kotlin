package org.novi.core

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.novi.activations.DateRangeData
import org.novi.activations.DateTimeActivationWithId
import org.novi.activations.WeightedRandomActivationWithId
import org.novi.persistence.BaseActivationWithId
import java.text.SimpleDateFormat
import java.util.*


class ServiceLoaderTestWithId {
    companion object {
        private const val config = """
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
    fun testServiceLoader() {
        val loader: ServiceLoader<BaseActivationWithId<*>> = ServiceLoader.load(BaseActivationWithId::class.java)
        Assertions.assertThat(loader).hasAtLeastOneElementOfType(DateTimeActivationWithId::class.java)
        Assertions.assertThat(loader).hasAtLeastOneElementOfType(WeightedRandomActivationWithId::class.java)

        for (activation in loader) {
            when (activation) {
                is DateTimeActivationWithId -> {
//                    Assertions.assertThat(activation.configuration).isNull()
                    val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm")
                    val mapper = jacksonObjectMapper().setDateFormat(sdf)
                    val drd = mapper.readValue<DateRangeData>(config)
                    Assertions.assertThat(activation.withConfiguration(config).parsedConfig).isEqualTo(drd)
                }

                is WeightedRandomActivationWithId -> {
//                    Assertions.assertThat(activation.configuration).isNull()
                    val newInstance = activation.withConfiguration(wrConfig.trimIndent())
                    Assertions.assertThat(newInstance.parsedConfig).isEqualTo(newInstance.valueOf(wrConfig))
                }
            }
        }
    }
}