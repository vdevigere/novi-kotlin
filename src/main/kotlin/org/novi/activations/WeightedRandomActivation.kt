package org.novi.activations

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.treeToValue
import org.apache.commons.math3.distribution.EnumeratedDistribution
import org.apache.commons.math3.random.JDKRandomGenerator
import org.apache.commons.math3.util.Pair
import org.novi.core.ActivationFactory
import org.novi.persistence.BaseActivation

class WeightedRandomActivation(
    id: Long? = null,
    configStr: String? = null
) :
    BaseActivation<List<Pair<String, Double>>>(id, configStr) {

    override fun valueOf(s: String): List<Pair<String, Double>> {
        val mapper = jacksonObjectMapper()
        val parsedConfig = mapper.readValue<Map<String, Double>>(s)
        return parsedConfig.entries.stream().map { Pair.create(it.key, it.value) }.toList()
    }

    override fun evaluate(context: String): Boolean {
        val mapper = jacksonObjectMapper()
        val root = mapper.readTree(context)
        val parsedContext: Map<String, *> = mapper.treeToValue(root)
        val contextMap = parsedContext[this.javaClass.canonicalName] as Map<*, *>
        val seed = contextMap["seed"] as Int
        val variantToCheck = contextMap["variantToCheck"] as String
        val rnd = JDKRandomGenerator(seed)
        val ed = EnumeratedDistribution(rnd, this.parsedConfig)
        return ed.sample() == variantToCheck
    }
}

class WeightedRandomActivationFactory: ActivationFactory{
    override fun withConfiguration(configuration: String): BaseActivation<*> {
        return WeightedRandomActivation(configStr = configuration)
    }

}