package com.example.kajikashiApp.service

import com.example.kajikashiApp.entity.Session
import com.example.kajikashiApp.repository.SessionRepository
import org.springframework.stereotype.Service

@Service
class SessionService(
    private val sessionRepository: SessionRepository
) {
    fun isValidSession(session: String): Boolean {
        val isSession = sessionRepository.findByToken(session)
        if (isSession != null) {
            return true
        }else {
            return false
        }
    }
}