package org.novi.core

import org.novi.persistence.BaseActivation

interface ActivationConfigAware {
    fun setConfiguration(configuration: String): BaseActivation<*>
}