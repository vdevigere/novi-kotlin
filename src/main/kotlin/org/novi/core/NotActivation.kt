package org.novi.core

class NotActivation<T> (val op1: BaseActivation<*>, override var configuration: T?): BaseActivation<T> {
    override fun valueOf(configStr: String): BaseActivation<T> {
        throw UnsupportedOperationException()
    }

    override fun evaluate(context: String): Boolean = !op1.evaluate(context) 
}