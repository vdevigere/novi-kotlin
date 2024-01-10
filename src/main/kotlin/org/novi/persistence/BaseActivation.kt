package org.novi.persistence

import org.novi.core.AndActivation
import org.novi.core.NotActivation
import org.novi.core.OrActivation
import org.novi.exceptions.IdNotFoundException
import kotlin.jvm.optionals.getOrNull

abstract class BaseActivation<T : Any>(
    private var id: Long? = null,
    configString: String? = null,
    dataVal: T? = null
) : ActivationConfigRepositoryAware<BaseActivation<T>> {


    protected lateinit var repository: ActivationConfigRepository
    private var configuration: String? = configString
    var parsedConfig: T? = dataVal
        get() = field ?: valueOfWrapper(
            configuration ?: lookupById(
                id ?: throw IdNotFoundException("Id has not been set")
            ) ?: throw IdNotFoundException("$id, not found in Db")
        )
        private set

    private fun lookupById(identifier: Long): String? {
        configuration = repository.findById(identifier).getOrNull()?.config
        return configuration
    }

    private fun valueOfWrapper(s: String): T {
        val temp = valueOf(s)
        parsedConfig = temp
        return temp
    }

    abstract fun valueOf(s: String): T

    override fun setActivationConfigRepository(repository: ActivationConfigRepository): BaseActivation<T> {
        this.repository = repository
        return this
    }

    abstract fun evaluate(context: String): Boolean

    infix fun and(that: BaseActivation<*>): BaseActivation<*> = AndActivation(dataValue = arrayOf(this, that))

    infix fun or(that: BaseActivation<*>): BaseActivation<*> = OrActivation(dataValue = arrayOf(this, that))

    operator fun not(): BaseActivation<String> = NotActivation(this)
}