package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.ConnectionEntity
import com.example.data.local.MessageEntity
import com.example.data.local.ReportEntity
import com.example.data.local.UserEntity
import com.example.data.security.PasswordHasher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.UUID

class BooksStudyRepository(private val database: AppDatabase) {

    private val userDao = database.userDao()
    private val connectionDao = database.connectionDao()
    private val messageDao = database.messageDao()
    private val reportDao = database.reportDao()

    suspend fun initializeDatabase() = withContext(Dispatchers.IO) {
        val count = userDao.getUserCount()
        if (count == 0) {
            seedInitialUsers()
        }
    }

    private suspend fun seedInitialUsers() {
        val initialPeers = listOf(
            Triple("Aarav Sharma", "aarav_s", "aarav@study.io") to ("Class 12 Physics & Math enthusiast. Passionate about robotics and clean code." to 0xFF3B82F6),
            Triple("Priya Patel", "priya_p", "priya@study.io") to ("Class 11 Chemistry & Biology. Preparing for medical entrance & loves reading novels." to 0xFF8B5CF6),
            Triple("Rohan Mehta", "rohan_m", "rohan@study.io") to ("Class 10 CBSE topper. Big fan of calculus, algorithms, and sci-fi books." to 0xFF10B981),
            Triple("Ananya Iyer", "ananya_i", "ananya@study.io") to ("Class 12 Literature & Humanities. Philosophy, history and writing essays." to 0xFFF59E0B),
            Triple("Vikram Malhotra", "vikram_m", "vikram@study.io") to ("Class 11 Computer Science student. Building Android and Kotlin applications." to 0xFFEC4899),
            Triple("Sneha Rao", "sneha_r", "sneha@study.io") to ("Class 9 Science & Art explorer. Love discussing space science and mechanics." to 0xFF06B6D4)
        )

        for (peer in initialPeers) {
            val fullName = peer.first.first
            val username = peer.first.second
            val email = peer.first.third
            val bio = peer.second.first
            val color = peer.second.second

            val salt = PasswordHasher.generateSalt()
            val passwordHash = PasswordHasher.hashPassword("Study@123", salt)
            val privacySalt = PasswordHasher.generateSalt()
            val privacyPasswordHash = PasswordHasher.hashPassword("1234", privacySalt)

            val user = UserEntity(
                id = UUID.randomUUID().toString(),
                email = email,
                username = username,
                fullName = fullName,
                passwordHash = passwordHash,
                salt = salt,
                privacyPasswordHash = privacyPasswordHash,
                privacySalt = privacySalt,
                bio = bio,
                avatarColor = color,
                onlineStatus = true,
                lastSeen = System.currentTimeMillis()
            )
            userDao.insertUser(user)
        }
    }

    suspend fun register(fullName: String, username: String, email: String, password: String): Result<UserEntity> = withContext(Dispatchers.IO) {
        val cleanEmail = email.trim().lowercase()
        val cleanUsername = username.trim().lowercase()

        if (cleanEmail.isEmpty() || !cleanEmail.contains("@") || !cleanEmail.contains(".")) {
            return@withContext Result.failure(IllegalArgumentException("Please enter a valid email address"))
        }
        if (cleanUsername.length < 3) {
            return@withContext Result.failure(IllegalArgumentException("Username must be at least 3 characters"))
        }
        if (password.length < 6) {
            return@withContext Result.failure(IllegalArgumentException("Password must be at least 6 characters"))
        }

        val existingEmail = userDao.getUserByEmail(cleanEmail)
        if (existingEmail != null) {
            return@withContext Result.failure(IllegalArgumentException("An account with this email already exists"))
        }
        val existingUsername = userDao.getUserByUsername(cleanUsername)
        if (existingUsername != null) {
            return@withContext Result.failure(IllegalArgumentException("This username is already taken"))
        }

        val salt = PasswordHasher.generateSalt()
        val passwordHash = PasswordHasher.hashPassword(password, salt)
        val newUser = UserEntity(
            id = UUID.randomUUID().toString(),
            email = cleanEmail,
            username = cleanUsername,
            fullName = fullName.trim(),
            passwordHash = passwordHash,
            salt = salt,
            bio = "Books Study member • Connecting & learning",
            avatarColor = pickRandomAvatarColor()
        )
        userDao.insertUser(newUser)
        Result.success(newUser)
    }

    suspend fun login(emailOrUsername: String, password: String): Result<UserEntity> = withContext(Dispatchers.IO) {
        val cleanInput = emailOrUsername.trim().lowercase()
        val user = if (cleanInput.contains("@")) {
            userDao.getUserByEmail(cleanInput)
        } else {
            userDao.getUserByUsername(cleanInput)
        }

        if (user == null) {
            return@withContext Result.failure(IllegalArgumentException("No user found with this email or username"))
        }

        val isValid = PasswordHasher.verifyPassword(password, user.salt, user.passwordHash)
        if (!isValid) {
            return@withContext Result.failure(IllegalArgumentException("Incorrect password"))
        }

        val updated = user.copy(onlineStatus = true, lastSeen = System.currentTimeMillis())
        userDao.updateUser(updated)
        Result.success(updated)
    }

    suspend fun resetPassword(email: String, newPassword: String): Result<Unit> = withContext(Dispatchers.IO) {
        val cleanEmail = email.trim().lowercase()
        val user = userDao.getUserByEmail(cleanEmail)
            ?: return@withContext Result.failure(IllegalArgumentException("No account found with this email"))

        if (newPassword.length < 6) {
            return@withContext Result.failure(IllegalArgumentException("Password must be at least 6 characters"))
        }

        val newSalt = PasswordHasher.generateSalt()
        val newHash = PasswordHasher.hashPassword(newPassword, newSalt)
        userDao.updateUser(user.copy(passwordHash = newHash, salt = newSalt))
        Result.success(Unit)
    }

    suspend fun setPrivacyPassword(userId: String, pin: String): Result<Unit> = withContext(Dispatchers.IO) {
        val user = userDao.getUserByIdSync(userId)
            ?: return@withContext Result.failure(IllegalArgumentException("User not found"))

        if (pin.length < 4) {
            return@withContext Result.failure(IllegalArgumentException("Privacy Password must be at least 4 digits"))
        }

        val salt = PasswordHasher.generateSalt()
        val hash = PasswordHasher.hashPassword(pin, salt)
        userDao.updateUser(user.copy(privacyPasswordHash = hash, privacySalt = salt))
        Result.success(Unit)
    }

    suspend fun verifyPrivacyPassword(userId: String, pin: String): Boolean = withContext(Dispatchers.IO) {
        val user = userDao.getUserByIdSync(userId) ?: return@withContext false
        val hash = user.privacyPasswordHash ?: return@withContext false
        val salt = user.privacySalt ?: return@withContext false
        PasswordHasher.verifyPassword(pin, salt, hash)
    }

    suspend fun resetPrivacyPassword(userId: String, accountPassword: String, newPin: String): Result<Unit> = withContext(Dispatchers.IO) {
        val user = userDao.getUserByIdSync(userId)
            ?: return@withContext Result.failure(IllegalArgumentException("User not found"))

        val isAccountPasswordValid = PasswordHasher.verifyPassword(accountPassword, user.salt, user.passwordHash)
        if (!isAccountPasswordValid) {
            return@withContext Result.failure(IllegalArgumentException("Account password verification failed"))
        }

        if (newPin.length < 4) {
            return@withContext Result.failure(IllegalArgumentException("New Privacy Password must be at least 4 digits"))
        }

        val salt = PasswordHasher.generateSalt()
        val hash = PasswordHasher.hashPassword(newPin, salt)
        userDao.updateUser(user.copy(privacyPasswordHash = hash, privacySalt = salt))
        Result.success(Unit)
    }

    fun getUserFlow(userId: String): Flow<UserEntity?> = userDao.getUserById(userId)
    suspend fun getUserSync(userId: String): UserEntity? = userDao.getUserByIdSync(userId)

    fun searchUsers(query: String, currentUserId: String): Flow<List<UserEntity>> {
        return if (query.isBlank()) {
            userDao.getAllOtherUsers(currentUserId)
        } else {
            userDao.searchUsers(query.trim(), currentUserId)
        }
    }

    fun getAllOtherUsers(currentUserId: String): Flow<List<UserEntity>> = userDao.getAllOtherUsers(currentUserId)

    fun getConnectionsForUser(userId: String): Flow<List<ConnectionEntity>> = connectionDao.getConnectionsForUser(userId)

    suspend fun getConnectionBetween(userA: String, userB: String): ConnectionEntity? = withContext(Dispatchers.IO) {
        connectionDao.getConnectionBetween(userA, userB)
    }

    fun getConnectionBetweenFlow(userA: String, userB: String): Flow<ConnectionEntity?> = connectionDao.getConnectionBetweenFlow(userA, userB)

    suspend fun sendConnectionRequest(senderId: String, receiverId: String) = withContext(Dispatchers.IO) {
        val existing = connectionDao.getConnectionBetween(senderId, receiverId)
        if (existing != null) {
            if (existing.status == "BLOCKED") return@withContext
            connectionDao.updateConnection(existing.copy(senderId = senderId, receiverId = receiverId, status = "PENDING", updatedAt = System.currentTimeMillis()))
        } else {
            val connection = ConnectionEntity(
                id = UUID.randomUUID().toString(),
                senderId = senderId,
                receiverId = receiverId,
                status = "PENDING"
            )
            connectionDao.insertConnection(connection)
        }
    }

    suspend fun acceptConnectionBetween(userA: String, userB: String) = withContext(Dispatchers.IO) {
        val existing = connectionDao.getConnectionBetween(userA, userB) ?: return@withContext
        connectionDao.updateConnection(existing.copy(status = "ACCEPTED", updatedAt = System.currentTimeMillis()))
    }

    suspend fun rejectConnectionBetween(userA: String, userB: String) = withContext(Dispatchers.IO) {
        val existing = connectionDao.getConnectionBetween(userA, userB) ?: return@withContext
        connectionDao.deleteConnection(existing.id)
    }

    suspend fun cancelConnectionBetween(userA: String, userB: String) = withContext(Dispatchers.IO) {
        val existing = connectionDao.getConnectionBetween(userA, userB) ?: return@withContext
        connectionDao.deleteConnection(existing.id)
    }

    suspend fun removeConnectionBetween(userA: String, userB: String) = withContext(Dispatchers.IO) {
        val existing = connectionDao.getConnectionBetween(userA, userB) ?: return@withContext
        connectionDao.deleteConnection(existing.id)
    }

    suspend fun blockUser(currentUserId: String, targetUserId: String) = withContext(Dispatchers.IO) {
        val existing = connectionDao.getConnectionBetween(currentUserId, targetUserId)
        if (existing != null) {
            connectionDao.updateConnection(existing.copy(senderId = currentUserId, receiverId = targetUserId, status = "BLOCKED", updatedAt = System.currentTimeMillis()))
        } else {
            val blockConn = ConnectionEntity(
                id = UUID.randomUUID().toString(),
                senderId = currentUserId,
                receiverId = targetUserId,
                status = "BLOCKED"
            )
            connectionDao.insertConnection(blockConn)
        }
    }

    suspend fun reportUser(reporterId: String, reportedUserId: String, category: String, description: String) = withContext(Dispatchers.IO) {
        val report = ReportEntity(
            id = UUID.randomUUID().toString(),
            reporterId = reporterId,
            reportedUserId = reportedUserId,
            category = category,
            description = description
        )
        reportDao.insertReport(report)
    }

    fun getChatId(userA: String, userB: String): String {
        return if (userA < userB) "${userA}_${userB}" else "${userB}_${userA}"
    }

    fun getMessagesForChat(chatId: String, isPrivate: Boolean): Flow<List<MessageEntity>> {
        return messageDao.getMessagesForChat(chatId, isPrivate)
    }

    fun getAllMessagesForUser(userId: String, isPrivate: Boolean): Flow<List<MessageEntity>> {
        return messageDao.getAllMessagesForUser(userId, isPrivate)
    }

    suspend fun sendMessage(
        senderId: String,
        receiverId: String,
        text: String,
        messageType: String = "TEXT",
        mediaUrl: String? = null,
        replyToId: String? = null,
        replyToText: String? = null,
        isPrivate: Boolean = false
    ): Result<MessageEntity> = withContext(Dispatchers.IO) {
        val connection = connectionDao.getConnectionBetween(senderId, receiverId)
        if (connection != null && connection.status == "BLOCKED") {
            return@withContext Result.failure(IllegalStateException("Cannot send message to this user"))
        }

        if (isPrivate) {
            if (connection == null || connection.status != "ACCEPTED") {
                return@withContext Result.failure(IllegalStateException("Private messaging requires an accepted connection"))
            }
        }

        val chatId = getChatId(senderId, receiverId)
        val message = MessageEntity(
            id = UUID.randomUUID().toString(),
            chatId = chatId,
            senderId = senderId,
            receiverId = receiverId,
            text = text,
            messageType = messageType,
            mediaUrl = mediaUrl,
            timestamp = System.currentTimeMillis(),
            status = "DELIVERED",
            replyToId = replyToId,
            replyToText = replyToText,
            isPrivate = isPrivate
        )
        messageDao.insertMessage(message)
        Result.success(message)
    }

    suspend fun deleteMessage(messageId: String) = withContext(Dispatchers.IO) {
        messageDao.deleteMessage(messageId)
    }

    suspend fun updateProfile(userId: String, fullName: String, bio: String, avatarColor: Long) = withContext(Dispatchers.IO) {
        val user = userDao.getUserByIdSync(userId) ?: return@withContext
        userDao.updateUser(user.copy(fullName = fullName.trim(), bio = bio.trim(), avatarColor = avatarColor))
    }

    suspend fun updatePrivacySettings(
        userId: String,
        showOnlineStatus: Boolean,
        showLastSeen: Boolean,
        readReceipts: Boolean,
        showTyping: Boolean,
        showMessagePreview: Boolean
    ) = withContext(Dispatchers.IO) {
        val user = userDao.getUserByIdSync(userId) ?: return@withContext
        userDao.updateUser(
            user.copy(
                showOnlineStatus = showOnlineStatus,
                showLastSeen = showLastSeen,
                readReceipts = readReceipts,
                showTyping = showTyping,
                showMessagePreview = showMessagePreview
            )
        )
    }

    suspend fun setUserOffline(userId: String) = withContext(Dispatchers.IO) {
        val user = userDao.getUserByIdSync(userId) ?: return@withContext
        userDao.updateUser(user.copy(onlineStatus = false, lastSeen = System.currentTimeMillis()))
    }

    private fun pickRandomAvatarColor(): Long {
        val colors = listOf(
            0xFF3B82F6, 0xFF8B5CF6, 0xFF10B981, 0xFFF59E0B, 0xFFEC4899,
            0xFF06B6D4, 0xFF6366F1, 0xFF14B8A6, 0xFFD946EF
        )
        return colors.random()
    }
}
