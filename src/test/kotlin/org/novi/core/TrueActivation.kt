package org.novi.core

import org.novi.persistence.BaseActivation

class TrueActivation(id: Long? = null) : BaseActivation<String>(id) {
    override fun valueOf(s: String): String = s

    override fun evaluate(context: String): Boolean = true
}