package com.example.kajikashiApp.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@Table(name = "tasks")
@EntityListeners(AuditingEntityListener::class)
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false,unique = true)
    val token: String,

    @ManyToOne
    @JoinColumn(name = "task_type_id", nullable = false)
    val taskType: TaskType,

    @Column(name = "expires_at", nullable = false)
    val expiresAt: Instant,

    @CreatedDate
    @Column(name = "created_at" , updatable = false)
    val createdAt: Instant? = null,
)
