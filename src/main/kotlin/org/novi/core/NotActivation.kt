package org.novi.core

class NotActivation<T>(val op1: BaseActivation<*>, override var configuration: T) : BaseActivation<T> {
    override fun evaluate(context: String): Boolean = !op1.evaluate(context)
}