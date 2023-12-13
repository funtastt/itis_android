package ru.kpfu.itis.android.asadullin.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.kpfu.itis.android.asadullin.data.db.entity.UserEntity
import ru.kpfu.itis.android.asadullin.model.UserModel

@Dao
interface UserDao {
    @Query("SELECT * from users WHERE user_id = :userId")
    fun getUserById(userId : Int) : UserEntity

    @Query("SELECT * from users WHERE email = :email")
    fun getUsersByEmail(email : String) : List<UserEntity>

    @Query("SELECT * from users WHERE phone_number = :phoneNumber")
    fun getUsersByPhoneNumber(phoneNumber : String) : List<UserEntity>

    @Query("SELECT * from users WHERE name = :username")
    fun getUsersByUsername(username : String) : List<UserEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertUserModel(userModel: UserEntity)

    @Query("UPDATE users SET name = :newUsername WHERE user_id = :userId")
    fun updateUsername(userId: Int, newUsername: String)

    @Query("UPDATE users SET email = :newEmail WHERE user_id = :userId")
    fun updateEmail(userId: Int, newEmail: String)

    @Query("UPDATE users SET phone_number = :newPhone WHERE user_id = :userId")
    fun updatePhoneNumber(userId: Int, newPhone: String)

    @Query("UPDATE users SET password = :newPassword WHERE user_id = :userId")
    fun updatePassword(userId: Int, newPassword: String)

    @Query("UPDATE users SET is_deleted = 1, deleted_at = :currentTime WHERE user_id = :userId")
    fun safeDeleteUser(userId: Int, currentTime : Long)
}