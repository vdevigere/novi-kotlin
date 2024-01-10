package org.novi.activations.factories

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.novi.REGISTRY
import org.novi.activations.NoOpActivation
import org.novi.core.*
import org.novi.persistence.ActivationConfigRepository
import org.novi.persistence.ActivationConfigRepositoryAware
import org.novi.persistence.BaseActivation
import java.text.SimpleDateFormat

class NoviOperationActivationFactory(val op: String? = null) : ActivationConfigAware,
    ActivationConfigRepositoryAware<ActivationConfigAware> {
    private val dateFormat: String = "dd-MM-yyyy hh:mm"
    private val simpleDateFormat = SimpleDateFormat(dateFormat)
    private val mapper: ObjectMapper = jacksonObjectMapper().setDateFormat(simpleDateFormat)

    private lateinit var repository: ActivationConfigRepository

    override fun setActivationConfigRepository(repository: ActivationConfigRepository): ActivationConfigAware {
        this.repository = repository
        return this
    }

    override fun setConfiguration(configuration: String): BaseActivation<*> {
        val operation = mapper.readValue<NoviOperation>(configuration)
        val optional1 = repository.findById(operation.activationIds[0])
        val op1 = if (optional1.isPresent) {
            val activation = optional1.get()
            val clazz = Class.forName(activation.name).kotlin
            val factory = REGISTRY.instance[clazz] as ActivationConfigAware
            factory.setConfiguration(activation.config)
        } else NoOpActivation()
        when (op ?: operation.operation) {
            "AND" -> {
                val optional2 = repository.findById(operation.activationIds[1])
                val op2 = if (optional2.isPresent) {
                    val activation = optional2.get()
                    val clazz = Class.forName(activation.name).kotlin
                    val factory = REGISTRY.instance[clazz] as ActivationConfigAware
                    factory.setConfiguration(activation.config)
                } else NoOpActivation()
                return AndActivation(dataValue = arrayOf(op1, op2))
            }

            "OR" -> {
                val optional2 = repository.findById(operation.activationIds[1])
                val op2 = if (optional2.isPresent) {
                    val activation = optional2.get()
                    val clazz = Class.forName(activation.name).kotlin
                    val factory = REGISTRY.instance[clazz] as ActivationConfigAware
                    factory.setConfiguration(activation.config)
                } else NoOpActivation()
                return OrActivation(dataValue = arrayOf(op1, op2))
            }

            "NOT" -> {
                return NotActivation(op1)
            }
        }
        return NoOpActivation()
    }
}