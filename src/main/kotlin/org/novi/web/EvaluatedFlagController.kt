package org.novi.web

import org.novi.REGISTRY
import org.novi.persistence.ActivationConfigRepository
import org.novi.persistence.ActivationConfigRepositoryAware
import org.novi.persistence.Flag
import org.novi.persistence.FlagRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/evaluatedFlags")
class EvaluatedFlagController(
    @param:Autowired private val flagRepository: FlagRepository,
    @param:Autowired private val activationConfigRepository: ActivationConfigRepository
) {

    @GetMapping("/{id}")
    fun getEvaluatedFlagById(@PathVariable(name = "id") id: Long, @RequestBody context: String): Flag? {
        var resultingStatus = true
        val flag: Flag = flagRepository.findById(id).orElse(Flag.EMPTY)
        for (activationConfigs in flag.activationConfigs) {
            val factory = REGISTRY.instance[Class.forName(activationConfigs.name).kotlin]
            if (factory is ActivationConfigRepositoryAware) {
                (factory as ActivationConfigRepositoryAware).setActivationConfigRepository(activationConfigRepository)
            }
            resultingStatus =
                resultingStatus && factory?.withConfiguration(activationConfigs.config)?.evaluate(context) ?: false
        }
        flag.status = resultingStatus
        return flag
    }
}