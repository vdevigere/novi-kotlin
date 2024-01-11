package org.novi.core

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.novi.REGISTRY
import org.novi.persistence.ActivationConfigRepository
import org.novi.persistence.ActivationConfigRepositoryAware
import org.novi.persistence.BaseActivation
import org.slf4j.LoggerFactory

class AndActivation(
    id: Long? = null,
    configString: String? = null,
    dataValue: Array<BaseActivation<*>>? = null
) : BaseActivation<Array<BaseActivation<*>>>(id, configString, dataValue) {

    private val logger = LoggerFactory.getLogger(AndActivation::class.java)

    override fun valueOf(s: String): Array<BaseActivation<*>> {
        val retVal = ArrayList<BaseActivation<*>>()
        //1. Parse s into an array of ids
        val ids = mapper.readValue<Array<Long>>(s)
        //2. Lookup the configs for each id and assemble into a list
        val found = repository.findAllById(ids.asList())
        for (ac in found) {
            val clazz = Class.forName(ac.name).kotlin
            val factory = REGISTRY.instance[clazz]
            val ba = factory?.setConfiguration(ac.config)
            if (ba != null) retVal.add(ba)
        }
        return retVal.toTypedArray()
    }

    override fun setActivationConfigRepository(repository: ActivationConfigRepository): BaseActivation<Array<BaseActivation<*>>> {
        this.repository = repository
        parsedConfig!!.forEach { it.setActivationConfigRepository(repository) }
        return this
    }

    override fun evaluate(context: String): Boolean {
        val retValue = parsedConfig!!.map { ba -> ba.evaluate(context) }.reduce { acc, next ->
            val result = acc && next
            logger.debug("$acc && $next = $result")
            result
        }
        return retValue
    }

    companion object : ActivationConfigAware, ActivationConfigRepositoryAware<ActivationConfigAware> {
        val mapper = jacksonObjectMapper();
        private lateinit var repository: ActivationConfigRepository

        override fun setConfiguration(configuration: String): BaseActivation<*> =
            AndActivation(configString = configuration).setActivationConfigRepository(this.repository)

        override fun setActivationConfigRepository(repository: ActivationConfigRepository): ActivationConfigAware {
            this.repository = repository
            return this
        }
    }
}

class AndActivationFactory : ActivationConfigAware by AndActivation.Companion,
    ActivationConfigRepositoryAware<ActivationConfigAware> by AndActivation.Companion