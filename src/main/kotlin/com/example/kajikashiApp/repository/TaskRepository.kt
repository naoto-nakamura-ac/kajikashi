package com.example.kajikashiApp.repository

import com.example.kajikashiApp.entity.Task
import org.springframework.data.repository.CrudRepository
import java.time.Instant

interface TaskRepository: CrudRepository<Task, String> {
    fun findAllByUserIdAndCreatedAtBetween(userId:Long?,from: Instant,to: Instant): List<Task>?
}