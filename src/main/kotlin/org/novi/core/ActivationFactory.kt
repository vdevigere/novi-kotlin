package org.novi.core

import org.novi.persistence.BaseActivation

interface ActivationFactory {
    fun withConfiguration(configuration: String): BaseActivation<*>
}