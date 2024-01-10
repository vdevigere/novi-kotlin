package org.novi.core

import org.novi.persistence.BaseActivation

class TrueActivation(
    id: Long? = null,
    configString: String? = null
) : BaseActivation<String>(id, configString) {
    override fun valueOf(s: String): String = s

    override fun evaluate(context: String): Boolean = true
}