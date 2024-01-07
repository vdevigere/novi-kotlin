package org.novi.persistence

import org.novi.core.AndActivationWithId
import org.novi.core.NoArg
import org.novi.core.NotActivationWithId
import org.novi.core.OrActivationWithId
import org.novi.exceptions.ConfigStringNotFoundException
import org.novi.exceptions.IdNotFoundException
import kotlin.reflect.full.createInstance

@NoArg
abstract class BaseActivationWithId<T : Any>(
    private var id: Long? = null,
    configuration: String? = null,
    parsedConfig: T? = null
) {

    private lateinit var repository: ActivationConfigRepository
    private var configuration: String? = configuration
        get() = field ?: run {
            val optional = repository.findById(id ?: throw IdNotFoundException("Id has not been set"))
            if (optional.isPresent) {
                return optional.get().config
            }
            throw IdNotFoundException("$id was not found in the database")
        }

    var parsedConfig: T? = parsedConfig
        get() = field ?: run {
            configuration?.let { valueOf(it) }
                ?: throw ConfigStringNotFoundException("configuration is null or has not been set")
        }
        private set

    abstract fun valueOf(s: String): T

    open fun setActivationConfigRepository(repository: ActivationConfigRepository): BaseActivationWithId<T> {
        this.repository = repository
        return this
    }

    open fun withConfiguration(configuration: String): BaseActivationWithId<T> {
        val newInstance: BaseActivationWithId<T> = this::class.createInstance()
        newInstance.configuration = configuration
        return newInstance
    }

    abstract fun evaluate(context: String): Boolean

    infix fun and(that: BaseActivationWithId<*>): BaseActivationWithId<String> = AndActivationWithId(this, that, id)

    infix fun or(that: BaseActivationWithId<*>): BaseActivationWithId<String> = OrActivationWithId(this, that, id)

    operator fun not(): BaseActivationWithId<String> = NotActivationWithId(this, id)
}