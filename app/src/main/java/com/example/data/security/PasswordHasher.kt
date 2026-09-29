package com.example.data.security

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

object PasswordHasher {

    fun generateSalt(): String {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return Base64.getEncoder().encodeToString(salt)
    }

    fun hashPassword(password: String, salt: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        digest.update(salt.toByteArray())
        val hash = digest.digest(password.toByteArray())
        return Base64.getEncoder().encodeToString(hash)
    }

    fun verifyPassword(password: String, salt: String, expectedHash: String): Boolean {
        val computedHash = hashPassword(password, salt)
        return computedHash == expectedHash
    }
}
