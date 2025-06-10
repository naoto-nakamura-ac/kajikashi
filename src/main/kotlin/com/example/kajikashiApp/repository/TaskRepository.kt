package com.example.kajikashiApp.repository

import com.example.kajikashiApp.entity.Task
import org.springframework.data.repository.CrudRepository

interface TaskRepository: CrudRepository<Task, String> {
}