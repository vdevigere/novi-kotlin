package org.novi

import org.novi.core.ActivationConfigAware
import org.springframework.context.annotation.Configuration
import java.util.*
import kotlin.reflect.KClass

@Configuration
class NoviConfiguration() {

    init {
        registerPlugins()
    }

    private final fun registerPlugins() {
        val loader = ServiceLoader.load(ActivationConfigAware::class.java)
        for (factory in loader) {
            REGISTRY.instance[factory::class] = factory
        }
    }
}

object REGISTRY {
    val instance = HashMap<KClass<out ActivationConfigAware>, ActivationConfigAware>()
}