package org.novi.persistence

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.novi.exceptions.IdNotFoundException
import java.util.*

class BaseActivationTest {

    data class DummyData(val id: Long, val value: String)
    class DummyActivation(id: Long? = null, configString: String? = null, dataVal: DummyData? = null) :
        BaseActivation<DummyData>(id, configString, dataVal) {
        override fun valueOf(s: String): DummyData = mapper.readValue(s)

        override fun evaluate(context: String): Boolean = context.toBoolean()

        companion object {
            val mapper = jacksonObjectMapper()
        }
    }

    @Test
    fun getParsedConfigAllNull() {
        val da = DummyActivation()
        assertThatThrownBy { da.parsedConfig }.isInstanceOf(IdNotFoundException::class.java)
            .hasMessage("Id has not been set")
    }

    @Test
    fun getParsedConfigDataValSet() {
        val dataVal = DummyData(1L, "dummy")
        val da = DummyActivation(dataVal = dataVal)
        assertSame(da.parsedConfig, dataVal)
    }

    @Test
    fun getParsedConfigConfigStrSet() {
        val dataVal = DummyData(1L, "dummy")
        val configStr = DummyActivation.mapper.writeValueAsString(dataVal)
        val da = DummyActivation(configString = configStr)
        assertThat(da.parsedConfig).isEqualTo(dataVal)
    }

    @Test
    fun getParsedConfigIdSet() {
        val dataVal = DummyData(1L, "dummy")
        val configStr = DummyActivation.mapper.writeValueAsString(dataVal)
        val ac = ActivationConfig(id = 1L, name = "dummy", description = "no desc", config = configStr)
        val mockRepo = mock(ActivationConfigRepository::class.java)
        `when`(mockRepo.findById(1L)).thenReturn(Optional.of(ac))
        val daNoRepo = DummyActivation(1L)
        assertThatThrownBy { daNoRepo.parsedConfig }.isInstanceOf(UninitializedPropertyAccessException::class.java)
            .hasMessage("lateinit property repository has not been initialized")
        val da = DummyActivation(id = 1L).setActivationConfigRepository(mockRepo)
        assertThat(da.parsedConfig).isEqualTo(dataVal)
    }

    @Test
    fun getParsedConfigUnknownId() {
        val mockRepo = mock(ActivationConfigRepository::class.java)
        val da = DummyActivation(id = 1L).setActivationConfigRepository(mockRepo)
        assertThatThrownBy { da.parsedConfig }.isInstanceOf(IdNotFoundException::class.java)
            .hasMessage("1, not found in Db")
    }

    @Test
    fun onlyOneCallToLookup() {
        val dataVal = DummyData(1L, "dummy")
        val configStr = DummyActivation.mapper.writeValueAsString(dataVal)
        val ac = ActivationConfig(id = 1L, name = "dummy", description = "no desc", config = configStr)
        val mockRepo = mock(ActivationConfigRepository::class.java)
        `when`(mockRepo.findById(1L)).thenReturn(Optional.of(ac))
        val da = DummyActivation(id = 1L).setActivationConfigRepository(mockRepo)
        val spy = spy(da)
        assertThat(da.parsedConfig).isEqualTo(dataVal)
        da.parsedConfig
        verify(mockRepo, times(1)).findById(1L)
    }

    @Test
    fun onlyOneCallToValueOf() {
        val dataVal = DummyData(1L, "dummy")
        val configStr = DummyActivation.mapper.writeValueAsString(dataVal)
        val da = DummyActivation(configString = configStr)
        val spy = spy(da)
        assertThat(spy.parsedConfig).isEqualTo(dataVal)
        spy.parsedConfig
        verify(spy, times(1)).valueOf(configStr)
    }
}