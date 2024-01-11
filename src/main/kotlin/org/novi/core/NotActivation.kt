package org.novi.core

import com.fasterxml.jackson.module.kotlin.readValue
import org.novi.REGISTRY
import org.novi.exceptions.IdNotFoundException
import org.novi.persistence.ActivationConfigRepository
import org.novi.persistence.ActivationConfigRepositoryAware
import org.novi.persistence.BaseActivation
import kotlin.jvm.optionals.getOrElse

class NotActivation(
    id: Long? = null,
    configString: String? = null,
    dataValue: BaseActivation<*>?=null
) : BaseActivation<BaseActivation<*>>(id, configString, dataValue) {
    override fun valueOf(s: String): BaseActivation<*> {
        val id = s.toLong()
        //2. Lookup the configs for each id and assemble into a list
        val found = repository.findById(id).getOrElse { throw IdNotFoundException("$id not found in Db") }
        val clazz = Class.forName(found.name).kotlin
        val factory = REGISTRY.instance[clazz]
        val ba = factory?.setConfiguration(found.config)?:throw UnsupportedOperationException("$clazz not registered")
        return ba
    }
    override fun setActivationConfigRepository(repository: ActivationConfigRepository): BaseActivation<BaseActivation<*>> {
        this.repository = repository
        parsedConfig!!.setActivationConfigRepository(repository)
        return this
    }

    override fun evaluate(context: String): Boolean = !parsedConfig!!.evaluate(context)

    companion object: ActivationConfigAware, ActivationConfigRepositoryAware<ActivationConfigAware>{
        private lateinit var repository: ActivationConfigRepository

        override fun setConfiguration(configuration: String): BaseActivation<*> =
            NotActivation(configString = configuration).setActivationConfigRepository(repository)

        override fun setActivationConfigRepository(repository: ActivationConfigRepository): ActivationConfigAware {
            this.repository = repository
            return this
        }
    }
}

class NotActivationFactory : ActivationConfigAware by NotActivation.Companion, ActivationConfigRepositoryAware<ActivationConfigAware> by NotActivation.Companion