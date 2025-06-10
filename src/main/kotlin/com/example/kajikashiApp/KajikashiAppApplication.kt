package com.example.kajikashiApp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class KajikashiAppApplication

fun main(args: Array<String>) {
	runApplication<KajikashiAppApplication>(*args)
}
