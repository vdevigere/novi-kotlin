package org.novi.core

import org.novi.activations.factories.NoviOperationActivationFactory
import org.novi.persistence.ActivationConfigRepository
import org.novi.persistence.ActivationConfigRepositoryAware
import org.novi.persistence.BaseActivation

class AndActivation(
    private val op1: BaseActivation<*>,
    private val op2: BaseActivation<*>,
) : BaseActivation<String>() {
    override fun valueOf(s: String): String = "( ${op1.parsedConfig} & ${op2.parsedConfig} )"
    override fun setActivationConfigRepository(repository: ActivationConfigRepository): BaseActivation<String> {
        op1.setActivationConfigRepository(repository)
        op2.setActivationConfigRepository(repository)
        return super.setActivationConfigRepository(repository)
    }

    override fun evaluate(context: String): Boolean = op1.evaluate(context) && op2.evaluate(context)
}

class AndActivationFactory : ActivationFactory, ActivationConfigRepositoryAware() {
    override fun withConfiguration(configuration: String): BaseActivation<*> =
        NoviOperationActivationFactory("AND").setActivationConfigRepository(repository).withConfiguration(configuration)


    override fun setActivationConfigRepository(repository: ActivationConfigRepository): ActivationFactory {
        this.repository = repository
        return this
    }
}