package org.novi.core

import org.novi.persistence.ActivationConfigRepository
import org.novi.persistence.BaseActivationWithId

class NotActivationWithId(private val op1: BaseActivationWithId<*>, id: Long?) : BaseActivationWithId<String>(id) {
    override fun valueOf(s: String): String = "!(${op1.parsedConfig})"
    override fun setActivationConfigRepository(repository: ActivationConfigRepository): BaseActivationWithId<String> {
        op1.setActivationConfigRepository(repository)
        return super.setActivationConfigRepository(repository)
    }

    override fun evaluate(context: String): Boolean = !op1.evaluate(context)
}