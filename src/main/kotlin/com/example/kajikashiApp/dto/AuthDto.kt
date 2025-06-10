package com.example.kajikashiApp.dto

import com.example.kajikashiApp.entity.Family

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String ,
    val familyCode: String
)

data class RegisterResponse(
    val email: String,
    val name: String,
    val familyID: Long?,
    val token: String,
)

data class LoginResponse(
    val email: String,
    val name: String,
    val family: Family?,
    val token: String,
)
data class LoginRequest(
    val email: String,
    val password: String
)

data class GetAuthResponse(
    val email: String?,
    val name: String?,
    val family: Family?,
)