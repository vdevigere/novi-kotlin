package org.novi.activations.factories

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.novi.core.*
import org.novi.persistence.ActivationConfigRepository
import org.novi.persistence.ActivationConfigRepositoryAware
import org.novi.persistence.BaseActivation
import java.text.SimpleDateFormat

class ComboBooleanActivation(id: Long?=null, configString: String?=null): BaseActivation<NoviOperation>(id, configString) {
    override fun valueOf(s: String): NoviOperation {
        return mapper.readValue<NoviOperation>(s)
    }

    override fun evaluate(context: String): Boolean {
        val ac = when(val operation = parsedConfig!!.operation){
            "AND"->{
                AndActivation(configString = mapper.writeValueAsString(parsedConfig!!.activationIds))
            }

            "OR"-> {
                OrActivation(configString = mapper.writeValueAsString(parsedConfig!!.activationIds))
            }

            "NOT"->{
                throw UnsupportedOperationException("Not not yet supported")
            }
            else ->{
                throw IllegalArgumentException("Unknown operation: $operation")
            }
        }
        return  ac.setActivationConfigRepository(repository).evaluate(context)
    }

    companion object : ActivationConfigAware, ActivationConfigRepositoryAware<ActivationConfigAware> {
        private const val DATE_FORMAT: String = "dd-MM-yyyy hh:mm"
        private val simpleDateFormat = SimpleDateFormat(DATE_FORMAT)
        private val mapper: ObjectMapper = jacksonObjectMapper().setDateFormat(simpleDateFormat)

        private lateinit var repository: ActivationConfigRepository

        override fun setConfiguration(configuration: String): BaseActivation<*> =
            ComboBooleanActivation(configString = configuration).setActivationConfigRepository(repository)


        override fun setActivationConfigRepository(repository: ActivationConfigRepository): ActivationConfigAware {
            this.repository = repository
            return this
        }
    }
}

class ComboBooleanActivationFactory : ActivationConfigAware by ComboBooleanActivation.Companion, ActivationConfigRepositoryAware<ActivationConfigAware> by ComboBooleanActivation.Companion