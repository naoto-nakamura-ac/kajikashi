package com.example.kajikashiApp.dto
data class TaskPoint(
    val category: String,
    val point: Long
)
data class SummaryResponse(
    val total: Long,
    val user: String,
    val byCategory:List<TaskPoint>
)