package org.novi.activations

import org.novi.core.ActivationConfigAware
import org.novi.persistence.BaseActivation

class NoOpActivation : BaseActivation<String>() {
    override fun valueOf(s: String): String = s

    override fun evaluate(context: String): Boolean = true

    companion object : ActivationConfigAware{
        override fun setConfiguration(configuration: String): BaseActivation<*> {
            val noOpActivation = NoOpActivation()
            noOpActivation.configuration = configuration
            return noOpActivation
        }

    }
}
class NoOpActivationFactory : ActivationConfigAware by NoOpActivation.Companion