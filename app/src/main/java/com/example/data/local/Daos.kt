package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserByIdSync(id: String): UserEntity?

    @Query("SELECT * FROM users WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE LOWER(username) = LOWER(:username) LIMIT 1")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Query("SELECT * FROM users WHERE id != :currentUserId AND (LOWER(fullName) LIKE '%' || LOWER(:query) || '%' OR LOWER(username) LIKE '%' || LOWER(:query) || '%')")
    fun searchUsers(query: String, currentUserId: String): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id != :currentUserId ORDER BY fullName ASC")
    fun getAllOtherUsers(currentUserId: String): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int
}

@Dao
interface ConnectionDao {
    @Query("SELECT * FROM connections WHERE senderId = :userId OR receiverId = :userId")
    fun getConnectionsForUser(userId: String): Flow<List<ConnectionEntity>>

    @Query("SELECT * FROM connections WHERE (senderId = :userA AND receiverId = :userB) OR (senderId = :userB AND receiverId = :userA) LIMIT 1")
    suspend fun getConnectionBetween(userA: String, userB: String): ConnectionEntity?

    @Query("SELECT * FROM connections WHERE (senderId = :userA AND receiverId = :userB) OR (senderId = :userB AND receiverId = :userA) LIMIT 1")
    fun getConnectionBetweenFlow(userA: String, userB: String): Flow<ConnectionEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConnection(connection: ConnectionEntity)

    @Update
    suspend fun updateConnection(connection: ConnectionEntity)

    @Query("DELETE FROM connections WHERE id = :id")
    suspend fun deleteConnection(id: String)
}

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE chatId = :chatId AND isPrivate = :isPrivate ORDER BY timestamp ASC")
    fun getMessagesForChat(chatId: String, isPrivate: Boolean): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE (senderId = :userId OR receiverId = :userId) AND isPrivate = :isPrivate ORDER BY timestamp DESC")
    fun getAllMessagesForUser(userId: String, isPrivate: Boolean): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Update
    suspend fun updateMessage(message: MessageEntity)

    @Query("DELETE FROM messages WHERE id = :id")
    suspend fun deleteMessage(id: String)
}

@Dao
interface ReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity)

    @Query("SELECT * FROM reports WHERE reporterId = :reporterId ORDER BY createdAt DESC")
    fun getReportsByReporter(reporterId: String): Flow<List<ReportEntity>>
}
