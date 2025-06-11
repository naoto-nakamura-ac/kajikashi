package com.example.kajikashiApp.service

import com.example.kajikashiApp.dto.TaskListResponce
import com.example.kajikashiApp.dto.TaskLogResponse
import com.example.kajikashiApp.dto.TaskRegisterRequest
import com.example.kajikashiApp.dto.TaskRegisterResponse
import com.example.kajikashiApp.dto.TaskSimple
import com.example.kajikashiApp.entity.Task
import com.example.kajikashiApp.entity.TaskCategory
import com.example.kajikashiApp.repository.TaskCategoryRepository
import com.example.kajikashiApp.repository.TaskRepository
import com.example.kajikashiApp.repository.TaskTypeRepository
import com.example.kajikashiApp.repository.UserRepository
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val taskTypeRepository: TaskTypeRepository,
    private val taskCategoryRepository: TaskCategoryRepository,
    private val userRepository: UserRepository
) {
    fun getTaskList(): List<TaskListResponce>?{
        val taskList = taskTypeRepository.findAll()
        return taskList.groupBy { it.category.id to it.category.name }
            .map { (categoryInfo, tasks) ->
                TaskListResponce(
                    categoryId = categoryInfo.first,
                    categoryName = categoryInfo.second,
                    tasks = tasks.map { TaskSimple(name = it.name, point = it.point) }
                )
            }
    }
    fun registerTasks(tasks: TaskRegisterRequest): TaskRegisterResponse? {
        val user = userRepository.findByEmail(tasks.email)
        val taskType = taskTypeRepository.findByName(tasks.taskTypeName)
        if(taskType != null){
            val task= Task(
                user= user,
                taskType = taskType
            )
            val saved = taskRepository.save(task)
            return TaskRegisterResponse(TaskSimple(saved.taskType!!.name,saved.taskType!!.point))
        }
        return null
    }

    fun getTaskLog(userid:Long?,from: String,to:String): List<TaskLogResponse>?{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val fromDate = LocalDate.parse(from,formatter).atStartOfDay(ZoneId.of("Asia/Tokyo")).toInstant()
        val toDate = LocalDate.parse(to,formatter).plusDays(1).atStartOfDay(ZoneId.of("Asia/Tokyo")).toInstant()
        val taskList = taskRepository.findAllByUserIdAndCreatedAtBetween(userid ,fromDate,toDate)
        if(!taskList.isNullOrEmpty()){
            val taskLog:List<TaskLogResponse> = taskList.map { task ->
                TaskLogResponse(
                    taskId = task.id,
                    userName = task.user!!.name,
                    taskTypeName = task.taskType!!.name,
                    categoryName = task.taskType!!.category.name,
                    point = task.taskType!!.point,
                    createdAt = Timestamp.from(ZonedDateTime.ofInstant(task.createdAt, ZoneId.of("Asia/Tokyo")).toInstant())
                )
            }
            println(taskLog)
            return taskLog
        }
        return null
    }

}