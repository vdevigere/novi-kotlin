package org.novi.core

import org.novi.persistence.ActivationConfigRepository
import org.novi.persistence.BaseActivation

class NotActivation(private val op1: BaseActivation<*>) : BaseActivation<String>() {
    override fun valueOf(s: String): String = "!(${op1.parsedConfig})"
    override fun setActivationConfigRepository(repository: ActivationConfigRepository): BaseActivation<String> {
        op1.setActivationConfigRepository(repository)
        return super.setActivationConfigRepository(repository)
    }

    override fun evaluate(context: String): Boolean = !op1.evaluate(context)
}