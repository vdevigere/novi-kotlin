package org.novi.core

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.novi.activations.DateTimeActivation
import java.util.*

class ServiceLoaderTest {

    @Test
    fun testServiceLoader() {
        val loader: ServiceLoader<BaseActivation<*>> = ServiceLoader.load(BaseActivation::class.java)
        assertThat(loader).hasAtLeastOneElementOfType(DateTimeActivation::class.java)
        for (activation in loader) {
            assertThat(activation.configuration).isNotNull
        }
    }
}