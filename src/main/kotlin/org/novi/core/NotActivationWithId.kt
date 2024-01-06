package org.novi.core

import org.novi.persistence.ActivationConfigRepository
import org.novi.persistence.BaseActivationWithId

class NotActivationWithId(private val op1: BaseActivationWithId<*>, id: Long) : BaseActivationWithId<String>(id) {
    override fun valueOf(s: String): String = "!(${op1.parsedConfig})"
    override fun lookup(activationConfigRepository: ActivationConfigRepository): BaseActivationWithId<String> {
        op1.lookup(activationConfigRepository)
        return super.lookup(activationConfigRepository)
    }

    override fun evaluate(context: String): Boolean = !op1.evaluate(context)
}