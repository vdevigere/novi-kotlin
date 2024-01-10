package org.novi.core

import org.novi.persistence.BaseActivation

class FalseActivation(
    id: Long? = null,
    configString: String? = null
) : BaseActivation<String>(id, configString) {
    override fun valueOf(s: String): String = s
    override fun evaluate(context: String): Boolean = false

    companion object : ActivationConfigAware {
        override fun setConfiguration(configuration: String): BaseActivation<*> =
            FalseActivation(configString = configuration)
    }
}

class FalseActivationFactory : ActivationConfigAware by FalseActivation.Companion