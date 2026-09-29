package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val email: String,
    val username: String,
    val fullName: String,
    val passwordHash: String,
    val salt: String,
    val privacyPasswordHash: String? = null,
    val privacySalt: String? = null,
    val bio: String = "",
    val avatarColor: Long = 0xFF3B82F6,
    val onlineStatus: Boolean = true,
    val lastSeen: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val showOnlineStatus: Boolean = true,
    val showLastSeen: Boolean = true,
    val readReceipts: Boolean = true,
    val showTyping: Boolean = true,
    val showMessagePreview: Boolean = true,
    val profileVisibility: String = "PUBLIC"
)

@Entity(tableName = "connections")
data class ConnectionEntity(
    @PrimaryKey val id: String,
    val senderId: String,
    val receiverId: String,
    val status: String, // PENDING, ACCEPTED, REJECTED, BLOCKED
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val chatId: String,
    val senderId: String,
    val receiverId: String,
    val text: String,
    val messageType: String = "TEXT", // TEXT, IMAGE, VOICE, FILE
    val mediaUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "SENT", // SENT, DELIVERED, SEEN
    val replyToId: String? = null,
    val replyToText: String? = null,
    val isPrivate: Boolean = false
)

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey val id: String,
    val reporterId: String,
    val reportedUserId: String,
    val category: String,
    val description: String,
    val createdAt: Long = System.currentTimeMillis(),
    val status: String = "PENDING"
)
