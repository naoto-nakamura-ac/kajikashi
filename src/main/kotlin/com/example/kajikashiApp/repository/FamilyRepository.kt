package com.example.kajikashiApp.repository

import com.example.kajikashiApp.entity.Family
import org.springframework.data.repository.CrudRepository

interface FamilyRepository: CrudRepository<Family, Long> {
    fun findByCode(code: String): Family?
}