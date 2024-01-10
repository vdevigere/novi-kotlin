package org.novi.activations

import org.novi.core.ActivationConfigAware
import org.novi.persistence.BaseActivation

class NoOpActivation(
    id: Long? = null,
    configString: String? = null
) : BaseActivation<String>(id, configString) {
    override fun valueOf(s: String): String = s

    override fun evaluate(context: String): Boolean = true

    companion object : ActivationConfigAware {
        override fun setConfiguration(configuration: String): BaseActivation<*> =
            NoOpActivation(configString = configuration)
    }
}

class NoOpActivationFactory : ActivationConfigAware by NoOpActivation.Companion