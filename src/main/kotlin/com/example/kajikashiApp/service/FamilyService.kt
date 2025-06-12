package com.example.kajikashiApp.service

import com.example.kajikashiApp.dto.FamilyRequest
import com.example.kajikashiApp.dto.FamilyResponse
import com.example.kajikashiApp.repository.FamilyRepository
import com.example.kajikashiApp.repository.UserRepository
import org.springframework.stereotype.Service

@Service
data class FamilyService(
    private val familyRepository: FamilyRepository,
    private val userRepository: UserRepository
){
    fun getFamily(familyID: Long):List<FamilyResponse>{
        val familyList = userRepository.findAllByFamilyId(familyID)
        return familyList!!.map{user ->
            FamilyResponse(
                name = user.name
            )
        }

    }
}
