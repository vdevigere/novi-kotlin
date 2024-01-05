package org.novi.core

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.novi.activations.DateRangeData
import org.novi.activations.DateTimeActivation
import java.text.SimpleDateFormat
import java.util.*

private const val config = """
                {
                    "startDateTime":"11-12-2023 12:00",
                    "endDateTime":"20-12-2023 12:00"
                }
            """

class ServiceLoaderTest {

    @Test
    fun testServiceLoader() {
        val loader: ServiceLoader<BaseActivation<*>> = ServiceLoader.load(BaseActivation::class.java)
        assertThat(loader).hasAtLeastOneElementOfType(DateTimeActivation::class.java)
        val sdf = SimpleDateFormat("dd-MM-yyyy hh:mm")
        val mapper = jacksonObjectMapper().setDateFormat(sdf)
        val drd = mapper.readValue<DateRangeData>(config)
        for (activation in loader) {
            if (activation is DateTimeActivation) {
                assertThat(activation.configuration).isNull()
                activation.configuration = config.trimIndent()
                assertThat(activation.parsedConfig).isEqualTo(drd)
            }
        }
    }
}