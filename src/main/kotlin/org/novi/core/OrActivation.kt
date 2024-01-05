package org.novi.core

class OrActivation(
    private val op1: BaseActivation<*>,
    private val op2: BaseActivation<*>,
    override var configuration: String? = "( ${op1.parsedConfig} | ${op2.parsedConfig} )"
) :
    BaseActivation<String> {
    override fun valueOf(s: String?): String? = configuration

    override fun evaluate(context: String): Boolean = op1.evaluate(context) || op2.evaluate(context)
}