package org.novi.persistence

interface ActivationConfigRepositoryAware<T> {
    fun setActivationConfigRepository(repository: ActivationConfigRepository): T
}