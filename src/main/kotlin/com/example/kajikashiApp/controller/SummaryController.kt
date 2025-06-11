package com.example.kajikashiApp.controller

import com.example.kajikashiApp.dto.SummaryResponse
import com.example.kajikashiApp.service.SummaryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
data class SummaryController(val summaryService: SummaryService){
    @GetMapping("/api/summary")
    fun getSummary(
        @RequestParam familyId: Long,
        @RequestParam from: String,
        @RequestParam to: String
    ): ResponseEntity<List<SummaryResponse>>{
        val result = summaryService.getSummary(familyId,from,to)
        println("==============================>>>>>")
        println(result)
        if(result != null){
            return ResponseEntity.status(HttpStatus.OK).body(result)
        }
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
