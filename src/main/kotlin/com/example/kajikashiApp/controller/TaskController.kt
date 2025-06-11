package com.example.kajikashiApp.controller

import com.example.kajikashiApp.dto.TaskListResponce
import com.example.kajikashiApp.dto.TaskLogResponse
import com.example.kajikashiApp.dto.TaskRegisterRequest
import com.example.kajikashiApp.service.AuthService
import com.example.kajikashiApp.service.TaskService
import jakarta.persistence.criteria.From
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.sql.Time
import java.sql.Timestamp

@RestController
class TaskController(val taskService: TaskService, private val authService: AuthService) {

    @GetMapping("/api/tasks")
    fun getTaskList():ResponseEntity<List<TaskListResponce>>{
        val result = taskService.getTaskList()
        return ResponseEntity.status(HttpStatus.OK).body(result)
    }

    @PostMapping("/api/tasks")
    fun registerTask(@RequestBody request: TaskRegisterRequest): ResponseEntity<Any>{
        val result = taskService.registerTasks(request)
        if(result != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(result)
        }
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }

    @GetMapping("/api/tasks/log")
    fun getTaskLog(
        @RequestParam from: String,
        @RequestParam to: String,
        @RequestHeader("Authorization") requestHeader: String
    ): ResponseEntity<List<TaskLogResponse>>{
        val user = authService.getAuth(requestHeader)
        val result = taskService.getTaskLog(user?.id,from,to)
        if(result != null){
            // サーバーサイドだと日本時間に変換されてるけど、クライアント側ではUTCに変換されてしまうのでクライアント側で変換してあげる
            return ResponseEntity.status(HttpStatus.OK).body(result)
        }
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}