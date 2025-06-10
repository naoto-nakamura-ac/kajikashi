package com.example.kajikashiApp.repository

import com.example.kajikashiApp.entity.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, Long> {
    fun findByEmail(email: String): User?
}