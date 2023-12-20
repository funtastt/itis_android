package ru.kpfu.itis.android.asadullin.model

import ru.kpfu.itis.android.asadullin.data.db.entity.UserEntity

data class UserModel(
    val phoneNumber: String,
    val name: String,
    val email: String,
    val password: String
) {
    companion object {
        fun fromUserEntity(userEntity: UserEntity) = UserModel(
            phoneNumber = userEntity.phoneNumber,
            name = userEntity.name,
            email = userEntity.email,
            password = userEntity.password
        )

    }
}