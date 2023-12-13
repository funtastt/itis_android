package ru.kpfu.itis.android.asadullin.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.kpfu.itis.android.asadullin.model.UserModel

@Entity(
    tableName = "users",
    indices = [Index("email")],
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "user_id") val userId: Int = 0,
    @ColumnInfo(name = "phone_number") val phoneNumber: String,
    @ColumnInfo(name = "name") val name: String,
    val email: String,
    val password: String,
    @ColumnInfo(
        name = "is_deleted",
        defaultValue = false.toString()
    ) val isDeleted: Boolean = false,
    @ColumnInfo(
        name = "deleted_at"
    ) val deletedAt : Long? = null
) {
    companion object {
        fun fromUserModel(userModel: UserModel) = UserEntity(
            phoneNumber = userModel.phoneNumber,
            name = userModel.name,
            email = userModel.email,
            password = userModel.password
        )

    }
}