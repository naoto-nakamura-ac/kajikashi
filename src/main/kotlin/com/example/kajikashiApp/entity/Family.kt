package com.example.kajikashiApp.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@Table(name = "family")
@EntityListeners(AuditingEntityListener::class)
data class Family(
    @Id
    @GeneratedValue(GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 100)
    val name: String,

    @Column(nullable=false, length=255)
    val code: String,

    @CreatedDate
    @Column(name = "created_at" , updatable = false)
    val createdAt: Instant? = null,
)
