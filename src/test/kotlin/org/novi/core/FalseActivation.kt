package org.novi.core

import org.novi.persistence.BaseActivation

class FalseActivation(id: Long? = null) : BaseActivation<String>(id) {
    override fun valueOf(s: String): String = s
    override fun evaluate(context: String): Boolean = false
}

class FalseActivationFactory : ActivationConfigAware {
    override fun setConfiguration(configuration: String): BaseActivation<*> {
        val fa = FalseActivation().setConfiguration(configuration = configuration)
        return fa
    }
}