package org.novi.core

import org.novi.persistence.BaseActivationWithId

class FalseActivation(id: Long?=null, configStr: String? =null) : BaseActivationWithId<String>(id, configStr) {
    override fun valueOf(s: String): String = s
    override fun evaluate(context: String): Boolean = false
}

class FalseActivationFactory : ActivationFactory{
    override fun withConfiguration(configuration: String): BaseActivationWithId<*> {
        val fa = FalseActivation(configStr = configuration)
        return fa
    }
}