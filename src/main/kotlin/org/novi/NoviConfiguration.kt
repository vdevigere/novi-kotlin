package org.novi

import org.novi.core.ActivationConfigAware
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.*
import kotlin.reflect.KClass


@Configuration
class NoviConfiguration(@Value("\${activations.plugin.dir}") final val plugin_dir: String = "./plugin_activations") {

    init {
        val pluginDir = File(plugin_dir)
        val fList = pluginDir.listFiles { file -> file.path.lowercase().endsWith(".jar") }
        val urls = fList?.map { file -> file.toURI().toURL() }?.toTypedArray()
        registerPlugins(urls)

    }

    private final fun registerPlugins(urls: Array<URL>?) {
        var loader = ServiceLoader.load(ActivationConfigAware::class.java)
        if(urls!=null){
            loader = ServiceLoader.load(ActivationConfigAware::class.java, URLClassLoader.newInstance(urls, NoviConfiguration::class.java.classLoader))
        }
        for (factory in loader) {
                REGISTRY.instance[factory::class] = factory
            }
    }
}

object REGISTRY {
    val instance = HashMap<KClass<out ActivationConfigAware>, ActivationConfigAware>()
}