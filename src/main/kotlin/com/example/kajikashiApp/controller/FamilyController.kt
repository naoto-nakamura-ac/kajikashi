package com.example.kajikashiApp.controller

import com.example.kajikashiApp.dto.FamilyRequest
import com.example.kajikashiApp.dto.FamilyResponse
import com.example.kajikashiApp.service.FamilyService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
data class FamilyController(val familyService: FamilyService){

    @GetMapping("/api/family")
    fun getFamily(@RequestParam familyID: Long): ResponseEntity<List<FamilyResponse>>{
        val result = familyService.getFamily(familyID)
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }
}
