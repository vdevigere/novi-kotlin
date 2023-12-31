package org.novi.core

class AndActivation<T> (val op1: BaseActivation<*>, val op2: BaseActivation<*>, override var configuration: T?): BaseActivation<T> {
    override fun valueOf(configStr: String): BaseActivation<T>{
        throw UnsupportedOperationException()
    }

    override fun evaluate(context: String): Boolean = op1.evaluate(context) && op2.evaluate(context)
}