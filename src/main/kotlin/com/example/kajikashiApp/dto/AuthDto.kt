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
    val familyCode: String,
    val id: Long?
)

data class LoginResponse(
    val email: String,
    val name: String,
    val familyID: Long?,
    val familyCode: String,
    val token: String,
    val id: Long?
)
data class LoginRequest(
    val email: String,
    val password: String
)

data class GetAuthResponse(
    val id: Long?,
    val email: String?,
    val name: String?,
    val familyID: Long?,
    val familyCode: String,
)