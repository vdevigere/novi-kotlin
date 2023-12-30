package org.novi.web

import org.novi.persistence.Flag
import org.novi.persistence.FlagRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/flags")
class FlagController(@Autowired val flagRepository: FlagRepository) {

    @GetMapping
    fun getAllFlags(): Iterable<Flag> {
        val retVal = flagRepository.findAll()
        return retVal
    }
}