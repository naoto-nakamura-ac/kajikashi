package com.example.kajikashiApp.service

import com.example.kajikashiApp.dto.LoginRequest
import com.example.kajikashiApp.dto.LoginResponse
import com.example.kajikashiApp.dto.RegisterRequest
import com.example.kajikashiApp.dto.RegisterResponse
import com.example.kajikashiApp.entity.Family
import com.example.kajikashiApp.entity.Session
import com.example.kajikashiApp.entity.User
import com.example.kajikashiApp.repository.FamilyRepository
import com.example.kajikashiApp.repository.SessionRepository
import com.example.kajikashiApp.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.RequestHeader
import java.time.ZonedDateTime
import java.util.UUID

fun generateRandomCode(length: Int = 5): String {
    val charset = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { charset.random() }
        .joinToString("")
}

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val familyRepository: FamilyRepository,
    private val sessionRepository: SessionRepository,
) {
    fun getAuth(): String {
        return "OK"
    }
    fun isCheck(request: RegisterRequest): Boolean {
        val findEmail = userRepository.findByEmail(request.email)
        if (findEmail != null) {
            return false
        }
        return true
    }
    fun register(request: RegisterRequest): RegisterResponse {
        val encoder = BCryptPasswordEncoder()
        val hashedPassword = encoder.encode(request.password)
        var familyCode:String
        if(request.familyCode == "null"){
            do {
                familyCode = generateRandomCode()
            } while (familyRepository.findByCode(familyCode) != null)
            val newFamily = Family(code = familyCode)
            familyRepository.save(newFamily)
        }else{
            familyCode = request.familyCode
        }

        val newFamily = familyRepository.findByCode(familyCode)
        val user= User(
            name = request.name,
            email = request.email,
            passwordHash = hashedPassword,
            family = newFamily
        )
        var familyID: Long? = 0
        if(newFamily != null){
            familyID = newFamily.id
        }

        val saved = userRepository.save(user)
        var token: String
        do {
            token = UUID.randomUUID().toString()
        } while (sessionRepository.findByToken(token) != null)

        val session = Session(
            user = user,
            token = token,
            expiresAt = ZonedDateTime.now().plusMonths(1).toInstant(),
        )
        sessionRepository.save(session)
        return RegisterResponse(
            email = saved.email,
            name = saved.name,
            familyID = familyID,
            token = token
        )
    }
    fun login(request: LoginRequest): LoginResponse? {
        val encoder = BCryptPasswordEncoder()
        val findUser = userRepository.findByEmail(request.email)
        val hashedPassword = findUser?.passwordHash
        val isValid = encoder.matches(request.password, hashedPassword)
        if (isValid && findUser != null) {
            var token: String
            do {
                token = UUID.randomUUID().toString()
            } while (sessionRepository.findByToken(token) != null)

            val session = Session(
                user = findUser,
                token = token,
                expiresAt = ZonedDateTime.now().plusMonths(1).toInstant(),
            )
            sessionRepository.save(session)
            return LoginResponse(
                email = findUser.email,
                name = findUser.name,
                token = token,
                familyID = findUser.family
            )
        }
        return null
    }
    @Transactional
    fun logout(authHeader: String):String? {
        val token = authHeader.removePrefix("Bearer ").takeIf { it.isNotBlank() }
        if(token != null){
            val findToken = sessionRepository.findByToken(token)
            if(findToken != null) {
                sessionRepository.delete(findToken)
                return "Success"
            }
        }
        return null
    }
}