package org.novi.core

import org.novi.persistence.ActivationConfigRepository
import org.novi.persistence.BaseActivationWithId

class OrActivationWithId(private val op1: BaseActivationWithId<*>, private val op2: BaseActivationWithId<*>, id: Long?) :
    BaseActivationWithId<String>(id) {

    override fun setActivationConfigRepository(repository: ActivationConfigRepository): BaseActivationWithId<String> {
        op1.setActivationConfigRepository(repository)
        op2.setActivationConfigRepository(repository)
        return super.setActivationConfigRepository(repository)
    }

    override fun valueOf(s: String): String = "( ${op1.parsedConfig} || ${op2.parsedConfig} )"
    override fun evaluate(context: String): Boolean = op1.evaluate(context) || op2.evaluate(context)
}