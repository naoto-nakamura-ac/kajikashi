package com.example.kajikashiApp.dto

import com.example.kajikashiApp.entity.Family


data class FamilyRequest(
    val familyID: Long
)

data class FamilyResponse(
    val name: String
)