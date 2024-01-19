package org.novi

import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.HelpFormatter
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import org.novi.core.ActivationConfigAware
import org.novi.persistence.ActivationConfig
import org.novi.persistence.ActivationConfigRepository
import org.novi.persistence.Flag
import org.novi.persistence.FlagRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.*
import kotlin.reflect.KClass


@Configuration
class NoviConfiguration(@Value("\${activations.plugin.dir}") val plugin_dir: String = "./plugin_activations") {
    private val logger = LoggerFactory.getLogger(NoviConfiguration::class.java)

    init {
        val pluginDir = File(plugin_dir)
        val fList = pluginDir.listFiles { file -> file.path.lowercase().endsWith(".jar") }
        val urls = fList?.map { file -> file.toURI().toURL() }?.toTypedArray()
        registerPlugins(urls)

    }

    private final fun registerPlugins(urls: Array<URL>?) {
        var loader = ServiceLoader.load(ActivationConfigAware::class.java)
        if (urls != null) {
            loader = ServiceLoader.load(
                ActivationConfigAware::class.java,
                URLClassLoader.newInstance(urls, NoviConfiguration::class.java.classLoader)
            )
        }
        for (factory in loader) {
            REGISTRY.instance[factory::class] = factory
        }
    }

    @Bean
    fun initializeDb(
        @Autowired flagRepository: FlagRepository,
        @Autowired activationConfigRepository: ActivationConfigRepository
    ): CommandLineRunner {
        logger.debug("Initializing Db....")
        return CommandLineRunner {
            logger.debug("Command line params: {}", it)
            val parser = DefaultParser()
            val cmd = parser.parse(options, it)
            if(cmd.hasOption(seedDb)){
                val ac1 = ActivationConfig(
                    1L, "org.novi.activations.DateTimeActivationFactory",
                    "DateTime", """{"startDateTime":"11-12-2023 12:00","endDateTime":"20-12-2023 12:00" }"""
                )
                val ac2 = ActivationConfig(
                    2L, "org.novi.activations.WeightedRandomActivationFactory",
                    "Always SAMPLE A", """{"SampleA":100.0,"SampleB":0,"SampleC":0}""",
                )
                val ac3 = ActivationConfig(
                    3L, "org.novi.activations.ComboBooleanActivationFactory",
                    "1 AND 2", """{"activationIds":[1,2],"operation":"AND"}""",
                )
                val ac4 = ActivationConfig(
                    4L, "org.novi.activations.ComboBooleanActivationFactory",
                    "1 OR 2", """{"activationIds":[1,2],"operation":"OR"}""",
                )
                val ac5 = ActivationConfig(
                    5L, "org.novi.core.AndActivationFactory",
                    "DateTimeActivation && WeightedRandomActivation", "[1,2]"
                )
                val ac6 = ActivationConfig(
                    6L, "org.novi.core.OrActivationFactory",
                    "DateTimeActivation && WeightedRandomActivation", "[1,2]",
                )

                val flag1 = Flag(1L, "Implicit AND ids 1, 2", false, setOf(ac1, ac2))
                val flag2 = Flag(2L, "Use BooleanActivFac to AND 1, 2", false, setOf(ac3))
                val flag3 = Flag(3L, "Use BooleanActivFac to OR 1, 2", false, setOf(ac4))
                val flag4 = Flag(4L, "Use AndActivFac to AND 1, 2", false, setOf(ac5))
                val flag5 = Flag(5L, "Use OrActivFac to OR 1, 2", false, setOf(ac6))
                val flag6 = Flag(6L, "featureF", false, Collections.emptySet())

                activationConfigRepository.saveAll(listOf(ac1, ac2, ac3, ac4, ac5, ac6))
                flagRepository.saveAll(listOf(flag1, flag2, flag3, flag4, flag5, flag6))
            }
            if(cmd.hasOption(help)){
               HelpFormatter().printHelp("novi", options)
            }
        }
    }

    companion object{
        private val seedDb = Option.builder().option("s").longOpt("seedDb").desc("Seed the database with sample data").build()
        private val help = Option.builder().option("h").longOpt("help").desc("Print this help message").build()
        private val options: Options = Options().addOption(seedDb).addOption(help)
    }
}

object REGISTRY {
    val instance = HashMap<KClass<out ActivationConfigAware>, ActivationConfigAware>()
}