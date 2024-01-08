package org.novi.core

import org.novi.persistence.BaseActivation

class FalseActivation(id: Long? = null, configStr: String? = null) : BaseActivation<String>(id, configStr) {
    override fun valueOf(s: String): String = s
    override fun evaluate(context: String): Boolean = false
}

class FalseActivationFactory : ActivationFactory {
    override fun withConfiguration(configuration: String): BaseActivation<*> {
        val fa = FalseActivation(configStr = configuration)
        return fa
    }
}