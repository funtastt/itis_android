package ru.kpfu.itis.android.asadullin.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kpfu.itis.android.asadullin.data.db.entity.UserEntity
import ru.kpfu.itis.android.asadullin.model.UserModel

@Dao
interface UserDao {
    @Query("SELECT * from users WHERE user_id = :userId")
    fun getUserById(userId : Int) : UserEntity?


    @Query("SELECT * from users WHERE email = :email")
    fun getUsersByEmail(email : String) : List<UserEntity>?

    @Query("SELECT * from users WHERE phone_number = :phoneNumber")
    fun getUsersByPhoneNumber(phoneNumber : String) : List<UserEntity>?

    @Query("SELECT * from users WHERE name = :username")
    fun getUsersByUsername(username : String) : List<UserEntity>?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertUserModel(userModel: UserEntity)
}