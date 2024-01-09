package org.novi.persistence

import org.novi.core.ActivationFactory

abstract class ActivationConfigRepositoryAware {
    protected lateinit var repository: ActivationConfigRepository
    abstract fun setActivationConfigRepository(repository: ActivationConfigRepository): ActivationFactory
}