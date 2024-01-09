package org.novi.persistence

import org.novi.core.ActivationFactory

abstract class ActivationConfigRepositoryAware<T> {
    protected lateinit var repository: ActivationConfigRepository
    abstract fun setActivationConfigRepository(repository: ActivationConfigRepository): T
}