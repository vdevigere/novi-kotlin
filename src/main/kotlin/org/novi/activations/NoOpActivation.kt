package org.novi.activations

import org.novi.persistence.BaseActivation

class NoOpActivation : BaseActivation<String>() {
    override fun valueOf(s: String): String = s

    override fun evaluate(context: String): Boolean = true
}