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
@Table(name = "users")
@EntityListeners(AuditingEntityListener::class)
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable=false, length=100, unique = true)
    val email: String,

    @Column(nullable = false, length = 100)
    val name: String,

    @ManyToOne
    @JoinColumn(name = "family_id", nullable = false)
    val family: Family?,

    @Column(name = "password_hash", nullable = false)
    val passwordHash: String,

    @CreatedDate
    @Column(name = "created_at" , updatable = false)
    var createdAt: Instant? = null,
)
