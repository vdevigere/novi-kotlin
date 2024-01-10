package org.novi.core

import org.novi.persistence.ActivationConfigRepository
import org.novi.persistence.BaseActivation

class OrActivation(
    private val op1: BaseActivation<*>,
    private val op2: BaseActivation<*>,
    id: Long? = null,
    configString: String? = null
) :
    BaseActivation<String>(id, configString) {

    override fun setActivationConfigRepository(repository: ActivationConfigRepository): BaseActivation<String> {
        op1.setActivationConfigRepository(repository)
        op2.setActivationConfigRepository(repository)
        return super.setActivationConfigRepository(repository)
    }

    override fun valueOf(s: String): String = "( ${op1.parsedConfig} || ${op2.parsedConfig} )"
    override fun evaluate(context: String): Boolean = op1.evaluate(context) || op2.evaluate(context)
}