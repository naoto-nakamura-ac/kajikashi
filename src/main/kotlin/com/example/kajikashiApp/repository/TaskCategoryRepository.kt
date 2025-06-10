package com.example.kajikashiApp.repository

import com.example.kajikashiApp.entity.TaskCategory
import org.springframework.data.repository.CrudRepository

interface TaskCategoryRepository: CrudRepository<TaskCategory, String> {
}