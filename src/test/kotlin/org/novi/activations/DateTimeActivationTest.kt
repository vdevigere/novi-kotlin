package org.novi.activations

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DateTimeActivationTest {

    @Test
    fun evaluateForDateInBetween() {
        val dta = DateTimeActivationWithId(configuration =
            """
                {
                    "startDateTime":"11-12-2023 12:00",
                    "endDateTime":"20-12-2023 12:00"
                }
                """
        )
        val result: Boolean = dta.evaluate(
            """
                {
                    "org.novi.activations.DateTimeActivationWithId.currentDateTime": "15-12-2023 12:00"
                }
                """
        )
        assertThat(result).isTrue
    }

    @Test
    fun evaluateForDateEqStartDate() {
        val dta = DateTimeActivationWithId(configuration =
            """
                {
                    "startDateTime":"11-12-2023 12:00",
                    "endDateTime":"20-12-2023 12:00"
                }
                """
        )
        val result: Boolean = dta.evaluate(
            """
                {
                    "org.novi.activations.DateTimeActivationWithId.currentDateTime": "11-12-2023 12:00"
                }
                """
        )
        assertThat(result).isTrue
    }

    @Test
    fun evaluateForDateEqEndDate() {
        val dta = DateTimeActivationWithId(configuration =
            """
                {
                    "startDateTime":"11-12-2023 12:00",
                    "endDateTime":"20-12-2023 12:00"
                }
                """
        )
        val result: Boolean = dta.evaluate(
            """
                {
                    "org.novi.activations.DateTimeActivationWithId.currentDateTime": "20-12-2023 12:00"
                }
                """
        )
        assertThat(result).isFalse
    }

    @Test
    fun evaluateForDateGtEndDate() {
        val dta = DateTimeActivationWithId(configuration =
            """
                {
                    "startDateTime":"11-12-2023 12:00",
                    "endDateTime":"20-12-2023 12:00"
                }
                """
        )
        val result: Boolean = dta.evaluate(
            """
                {
                    "org.novi.activations.DateTimeActivationWithId.currentDateTime": "25-12-2023 12:00"
                }
                """
        )
        assertThat(result).isFalse
    }

    @Test
    fun evaluateForDateLtStartDate() {
        val dta = DateTimeActivationWithId(configuration =
            """
                {
                    "startDateTime":"11-12-2023 12:00",
                    "endDateTime":"20-12-2023 12:00"
                }
                """
        )
        val result: Boolean = dta.evaluate(
            """
                {
                    "org.novi.activations.DateTimeActivationWithId.currentDateTime": "05-12-2023 12:00"
                }
                """
        )
        assertThat(result).isFalse
    }
}