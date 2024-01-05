package org.novi.core

class NotActivation(
    private val op1: BaseActivation<*>,
    override var configuration: String = "!(${op1.parsedConfig})"
) : BaseActivation<String> {
    override fun valueOf(s: String): String = configuration

    override fun evaluate(context: String): Boolean = !op1.evaluate(context)
}