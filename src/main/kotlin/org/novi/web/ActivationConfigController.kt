package org.novi.web

import org.novi.persistence.ActivationConfig
import org.novi.persistence.ActivationConfigRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/activations")
class ActivationConfigController(@Autowired val activationConfigRepository: ActivationConfigRepository) {

    @GetMapping
    fun getAllActivations():List<ActivationConfig>{
        return activationConfigRepository.findAll()
    }

    @PostMapping
    fun createActivation(@RequestBody acData: ActivationConfigData): ActivationConfig{
        return activationConfigRepository.save(acData.toActivationConfig())
    }

    data class ActivationConfigData(val config: String, val description: String, val name: String){

        fun toActivationConfig(): ActivationConfig{
            return ActivationConfig(config = config, name = name, description = description)
        }
    }
}