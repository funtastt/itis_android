package ru.kpfu.itis.android.asadullin.data.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.kpfu.itis.android.asadullin.data.db.dao.MovieDao
import ru.kpfu.itis.android.asadullin.data.db.dao.UserDao
import ru.kpfu.itis.android.asadullin.data.db.dao.UserMovieInteractionDao
import ru.kpfu.itis.android.asadullin.data.db.entity.MovieEntity
import ru.kpfu.itis.android.asadullin.data.db.entity.UserEntity
import ru.kpfu.itis.android.asadullin.data.db.entity.UserMovieInteractionEntity

@Database(
    entities = [UserEntity::class, MovieEntity::class, UserMovieInteractionEntity::class],
    version = 1
)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract val userDao : UserDao
    abstract val movieDao : MovieDao
    abstract val interactionDao : UserMovieInteractionDao
}