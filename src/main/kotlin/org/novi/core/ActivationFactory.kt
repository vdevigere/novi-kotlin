package org.novi.core

import org.novi.persistence.BaseActivationWithId

interface ActivationFactory {
    fun withConfiguration(configuration: String): BaseActivationWithId<*>
}