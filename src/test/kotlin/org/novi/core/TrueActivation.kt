package org.novi.core

import org.novi.persistence.BaseActivationWithId

class TrueActivation(id: Long?) : BaseActivationWithId<String>(id) {
    constructor():this(null){

    }
    override fun valueOf(s: String): String = s

    override fun evaluate(context: String): Boolean = true

}