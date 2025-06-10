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
@Table(name = "task_types")
@EntityListeners(AuditingEntityListener::class)
data class TaskType(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "category_id")
    val category: TaskCategory,

    @Column(nullable = false, length = 100)
    val name: String,

    @Column(nullable = false)
    val point: Long,

    @CreatedDate
    @Column(name = "created_at" , updatable = false)
    val createdAt: Instant? = null,
    )
