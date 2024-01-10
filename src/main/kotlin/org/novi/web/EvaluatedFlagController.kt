package org.novi.web

import org.novi.REGISTRY
import org.novi.persistence.ActivationConfigRepository
import org.novi.persistence.ActivationConfigRepositoryAware
import org.novi.persistence.Flag
import org.novi.persistence.FlagRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/evaluatedFlags")
class EvaluatedFlagController(
    @param:Autowired private val flagRepository: FlagRepository,
    @param:Autowired private val activationConfigRepository: ActivationConfigRepository
) {

    private val logger = LoggerFactory.getLogger(EvaluatedFlagController::class.java)

    @GetMapping("/{id}")
    fun getEvaluatedFlagById(@PathVariable(name = "id") id: Long, @RequestBody context: String): Flag? {
        val flag: Flag = flagRepository.findById(id).orElse(Flag.EMPTY)
        val resultingStatus = flag.activationConfigs.map {
            val factory = REGISTRY.instance[Class.forName(it.name).kotlin]
            if (factory is ActivationConfigRepositoryAware<*>) {
                (factory as ActivationConfigRepositoryAware<*>).setActivationConfigRepository(activationConfigRepository)
            }
            val result =factory?.setConfiguration(it.config)?.evaluate(context) ?: false
            logger.debug("Result of ${it.name} is $result")
            result
        }.reduce { acc, next ->
            val result = acc && next
            logger.debug("$acc && $next = $result")
            result
        }
        logger.debug("Final status: $resultingStatus")
        flag.status = resultingStatus
        return flag
    }
}