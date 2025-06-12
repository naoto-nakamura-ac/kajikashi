package com.example.kajikashiApp.controller


import com.example.kajikashiApp.dto.GetAuthResponse
import com.example.kajikashiApp.dto.LoginRequest
import com.example.kajikashiApp.dto.LoginResponse
import com.example.kajikashiApp.dto.RegisterRequest
import com.example.kajikashiApp.dto.RegisterResponse
import com.example.kajikashiApp.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(val authService: AuthService) {

    @GetMapping("/api/auth/me")
    fun getMe(@RequestHeader("Authorization") requestHeader: String): ResponseEntity<GetAuthResponse>{
        val result = authService.getAuth(requestHeader)
        if(result != null){
            return ResponseEntity.status(HttpStatus.OK).body(result)
        }
        return ResponseEntity(HttpStatus.UNAUTHORIZED)
    }
    @PostMapping("/api/auth/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<RegisterResponse> {
        val isCheck = authService.isCheck(request)
        println(isCheck)
        if(!isCheck) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        val result = authService.register(request)
        if(result == null){
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result)
    }
    @PostMapping("/api/auth/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val result= authService.login(request)
        if(result != null){
            return ResponseEntity.status(HttpStatus.OK).body(result)
        }else{
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }
    @DeleteMapping("/api/auth/logout")
    fun logout(@RequestHeader("Authorization") requestHeader: String): ResponseEntity<String> {
        val result= authService.logout(requestHeader)
        if(result != null){
            return ResponseEntity(HttpStatus.OK)
        }else{
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
    }
}