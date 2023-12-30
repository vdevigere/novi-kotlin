package org.novi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NoviKotlinApplication

fun main(args: Array<String>) {
    runApplication<NoviKotlinApplication>(*args)
}
