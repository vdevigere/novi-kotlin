package org.novi.web

import org.novi.persistence.ActivationConfig
import org.novi.persistence.ActivationConfigRepository
import org.novi.persistence.Flag
import org.novi.persistence.FlagRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Collections

@RestController
@RequestMapping("/flags")
class FlagController(@Autowired val flagRepository: FlagRepository, @Autowired val activationConfigRepository: ActivationConfigRepository) {

    @GetMapping
    fun getAllFlags(): Iterable<Flag> {
        val retVal = flagRepository.findAll()
        return retVal
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): Flag {
        return flagRepository.getReferenceById(id)
    }

    @PostMapping
    fun saveFlag(@RequestBody flagData: FlagData): Flag{
        val flag = flagData.toFlag(activationConfigRepository)
        return flagRepository.save(flag)
    }

    data class FlagData(val name: String, val activationIds: Set<Long> = Collections.emptySet()){
        fun toFlag(activationConfigRepository: ActivationConfigRepository):Flag{
            val activationConfigs: Set<ActivationConfig> = activationIds.map { activationConfigRepository.getReferenceById(it) }.toSet()
            return Flag(name = this.name, activationConfigs = activationConfigs)
        }
    }
}