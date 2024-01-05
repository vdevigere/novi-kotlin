package org.novi.core

@NoArg
interface BaseActivation<T> {

    var configuration: String?

    val parsedConfig: T?
        get() = valueOf(configuration)

    fun valueOf(s: String?): T?

    fun evaluate(context: String): Boolean

    infix fun and(that: BaseActivation<*>): BaseActivation<*> =
        AndActivation(this, that)

    infix fun or(that: BaseActivation<*>): BaseActivation<*> =
        OrActivation(this, that)

    operator fun not(): BaseActivation<*> = NotActivation(this)
}