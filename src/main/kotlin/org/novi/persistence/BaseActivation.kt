package org.novi.persistence

import org.novi.core.AndActivation
import org.novi.core.NotActivation
import org.novi.core.OrActivation
import org.novi.exceptions.IdNotFoundException
import kotlin.jvm.optionals.getOrElse

abstract class BaseActivation<T : Any>(
    private var id: Long? = null,
    private val configString: String? = null
) : ActivationConfigRepositoryAware<BaseActivation<T>> {


    private lateinit var repository: ActivationConfigRepository
    private lateinit var configuration: String

    val parsedConfig: T
        get() {
            if (!this::configuration.isInitialized) {
                configuration =
                    configString ?: repository.findById(id ?: throw IdNotFoundException("Id has not been set"))
                        .getOrElse { throw IdNotFoundException("Id not found in database") }.config
            }
            return valueOf(configuration)
        }


    abstract fun valueOf(s: String): T

    override fun setActivationConfigRepository(repository: ActivationConfigRepository): BaseActivation<T> {
        this.repository = repository
        return this
    }

    abstract fun evaluate(context: String): Boolean

    infix fun and(that: BaseActivation<*>): BaseActivation<String> = AndActivation(this, that)

    infix fun or(that: BaseActivation<*>): BaseActivation<String> = OrActivation(this, that)

    operator fun not(): BaseActivation<String> = NotActivation(this)
}