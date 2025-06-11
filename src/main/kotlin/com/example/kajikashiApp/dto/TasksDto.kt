package com.example.kajikashiApp.dto

import com.example.kajikashiApp.entity.Task
import com.example.kajikashiApp.entity.TaskCategory
import com.example.kajikashiApp.entity.TaskType
import java.sql.Timestamp

data class TaskSimple(
    val name: String,
    val point: Long
)
data class TaskListResponce(
    val categoryId: Long?,
    val categoryName: String,
    val tasks: List<TaskSimple>
)
data class TaskRegisterRequest(
    val taskTypeName: String,
    val email: String,
)

data class TaskRegisterResponse(
    val task: TaskSimple
)

data class TaskLogResponse(
    val taskId: Long?,
    val userName: String,
    val taskTypeName: String,
    val categoryName: String,
    val point: Long,
    val createdAt: Timestamp
)