package org.novi.core

interface BaseActivation<T> {
    var configuration: T?

    fun valueOf(configStr: String): BaseActivation<T>

    fun evaluate(context: String): Boolean

    infix fun and(that: BaseActivation<*>): BaseActivation<*> = AndActivation(this, that, "( ${this.configuration} & ${that.configuration} )")

    infix fun or(that: BaseActivation<*>): BaseActivation<*> = OrActivation(this, that, "( ${this.configuration} | ${that.configuration} )")

    operator fun not(): BaseActivation<*> = NotActivation(this, "!(${this.configuration})")
}