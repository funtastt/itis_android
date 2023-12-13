package ru.kpfu.itis.android.asadullin.data.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MovieWithUsers(
    @Embedded val movie: MovieEntity,
    @Relation(
        parentColumn = "movie_id",
        entityColumn = "user_id",
        associateBy = Junction(UserMovieInteractionEntity::class)
    )
    val users: List<UserEntity>
)