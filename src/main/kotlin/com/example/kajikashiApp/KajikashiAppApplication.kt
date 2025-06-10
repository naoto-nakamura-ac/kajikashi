package com.example.kajikashiApp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@Configuration
@EnableJpaAuditing
@SpringBootApplication
class KajikashiAppApplication

fun main(args: Array<String>) {
	runApplication<KajikashiAppApplication>(*args)
}
