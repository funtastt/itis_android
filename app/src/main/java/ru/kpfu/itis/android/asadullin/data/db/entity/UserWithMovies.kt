package ru.kpfu.itis.android.asadullin.data.db.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class UserWithMovies(
    @Embedded val user: UserEntity,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "movie_id",
        associateBy = Junction(UserMovieInteractionEntity::class)
    )
    val movies: List<MovieEntity>
)