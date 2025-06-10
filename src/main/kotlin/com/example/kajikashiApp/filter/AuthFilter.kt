package com.example.kajikashiApp.filter

import com.example.kajikashiApp.service.SessionService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AuthFilter(
    private val sessionService: SessionService,
): OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.servletPath
        return path.startsWith("/api/auth/login") || path.startsWith("/api/auth/register")
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ){
        val authHeader = request.getHeader("Authorization") ?: ""
        val token = authHeader.removePrefix("Bearer ").takeIf { it.isNotBlank() }

        if(token == null || !sessionService.isValidSession(token)) {
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            return
        }
        filterChain.doFilter(request, response)
    }
}