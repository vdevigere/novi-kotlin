package org.novi.core

import org.novi.persistence.BaseActivationWithId

class TrueActivation(id: Long?=null, configStr: String? = null) : BaseActivationWithId<String>(id, configStr) {
    override fun valueOf(s: String): String = s

    override fun evaluate(context: String): Boolean = true
}