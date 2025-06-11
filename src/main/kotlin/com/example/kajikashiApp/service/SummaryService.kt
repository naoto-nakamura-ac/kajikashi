package com.example.kajikashiApp.service

import com.example.kajikashiApp.dto.SummaryResponse
import com.example.kajikashiApp.dto.TaskPoint
import com.example.kajikashiApp.repository.FamilyRepository
import com.example.kajikashiApp.repository.UserRepository
import org.springframework.stereotype.Service

@Service
data class SummaryService(
    private val familyRepository: FamilyRepository,
    private val taskService: TaskService,
    private val userRepository: UserRepository
){
    fun getSummary(familyId: Long, from: String, to:String): List<SummaryResponse>?{
        val familyList = userRepository.findAllByFamilyId(familyId)
        if(!familyList.isNullOrEmpty()){
            return familyList.map { user ->
                val taskList = taskService.getTaskLog(user.id,from,to)
                val byCategory= taskList!!
                    .groupBy { it.categoryName }
                    .map { (category,task) ->
                        TaskPoint(
                            category = category,
                            point = task.sumOf { it.point }
                        )
                    }
                val total = taskList.sumOf { it.point }
                SummaryResponse(
                    total=total,
                    user=user.name,
                    byCategory=byCategory
                )
            }
        }
        return null

    }
}
