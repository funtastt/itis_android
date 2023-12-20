package ru.kpfu.itis.android.asadullin.utils

import org.mindrot.jbcrypt.BCrypt


object PasswordEncryptor {
    fun encryptPassword(password: String): String {
        val salt = BCrypt.gensalt()
        return BCrypt.hashpw(password, salt)
    }

    fun checkPassword(candidate: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(candidate, hashedPassword)
    }
}