package org.novi.persistence

import org.novi.core.AndActivationWithId
import org.novi.core.BaseActivation
import org.novi.core.NotActivationWithId
import org.novi.core.OrActivationWithId

abstract class BaseActivationWithId<T : Any>(val id: Long) {

    lateinit var configuration: String
    val parsedConfig: T
        get() = valueOf(configuration)

    abstract fun valueOf(s: String): T

    open fun lookup(activationConfigRepository: ActivationConfigRepository): BaseActivationWithId<T> {
        if(!this::configuration.isInitialized) {
            val fromDb = activationConfigRepository.findById(this.id)
            if (fromDb.isPresent) {
                this.configuration = fromDb.get().config
            }
        }
        return this
    }

    abstract fun evaluate(context: String): Boolean

    infix fun and(that: BaseActivationWithId<*>): BaseActivationWithId<String> = AndActivationWithId(this, that, id)

    infix fun or(that: BaseActivationWithId<*>): BaseActivationWithId<String> = OrActivationWithId(this, that, id)

    operator fun not(): BaseActivationWithId<String> = NotActivationWithId(this, id)
}