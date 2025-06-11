package com.example.kajikashiApp.repository

import com.example.kajikashiApp.entity.Family
import com.example.kajikashiApp.entity.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun findAllByFamilyId(familyId: Long): List<User>?
}