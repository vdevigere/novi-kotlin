package org.novi.persistence

import org.novi.core.AndActivation
import org.novi.core.NoArg
import org.novi.core.NotActivation
import org.novi.core.OrActivation
import org.novi.exceptions.ConfigStringNotFoundException
import org.novi.exceptions.IdNotFoundException

@NoArg
abstract class BaseActivation<T : Any>(
    private var id: Long? = null,
    configStr: String? = null
) {

    private lateinit var repository: ActivationConfigRepository
    private var configuration: String? = configStr
        get() = field ?: run {
            val optional = repository.findById(id ?: throw IdNotFoundException("Id has not been set"))
            if (optional.isPresent) {
                return optional.get().config
            }
            throw IdNotFoundException("$id was not found in the database")
        }

    val parsedConfig: T by lazy {
        configuration?.let { valueOf(it) }
            ?: throw ConfigStringNotFoundException("configuration is null or has not been set")
    }

    abstract fun valueOf(s: String): T

    open fun setActivationConfigRepository(repository: ActivationConfigRepository): BaseActivation<T> {
        this.repository = repository
        return this
    }

    abstract fun evaluate(context: String): Boolean

    infix fun and(that: BaseActivation<*>): BaseActivation<String> = AndActivation(this, that)

    infix fun or(that: BaseActivation<*>): BaseActivation<String> = OrActivation(this, that)

    operator fun not(): BaseActivation<String> = NotActivation(this)
}