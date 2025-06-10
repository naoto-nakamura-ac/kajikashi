package com.example.kajikashiApp.repository

import com.example.kajikashiApp.entity.Session
import org.springframework.data.repository.CrudRepository

interface SessionRepository: CrudRepository<Session, Long> {
    fun findByToken(token: String): Session?
}