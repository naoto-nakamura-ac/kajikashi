package com.example.kajikashiApp.repository

import com.example.kajikashiApp.entity.TaskType
import org.springframework.data.repository.CrudRepository

interface TaskTypeRepository: CrudRepository<TaskType, String> {
    fun findByName(name:String): TaskType?
}