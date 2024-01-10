package org.novi.core

import org.novi.persistence.BaseActivation

class FalseActivation(id: Long? = null) : BaseActivation<String>(id) {
    override fun valueOf(s: String): String = s
    override fun evaluate(context: String): Boolean = false

    companion object: ActivationConfigAware{
        override fun setConfiguration(configuration: String): BaseActivation<*> {
            val fa = FalseActivation()
            fa.configuration = configuration
            return fa
        }
    }
}
class FalseActivationFactory : ActivationConfigAware by FalseActivation.Companion