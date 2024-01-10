package org.novi.persistence

import org.novi.core.*
import org.novi.exceptions.IdNotFoundException

@NoArg
abstract class BaseActivation<T : Any>(
    private var id: Long? = null,
) : ActivationConfigRepositoryAware<BaseActivation<T>>, ActivationConfigAware {


    private lateinit var repository: ActivationConfigRepository
    protected lateinit var configuration: String

    val parsedConfig: T
        get() {
            if (!this::configuration.isInitialized) {
                val optional = repository.findById(id ?: throw IdNotFoundException("Id has not been set"))
                if (optional.isPresent) {
                    this.configuration = optional.get().config
                }
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
    override fun setConfiguration(configuration: String): BaseActivation<*> {
        this.configuration = configuration
        return this
    }
}