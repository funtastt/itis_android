package ru.kpfu.itis.android.asadullin.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey @ColumnInfo(name = "movie_id") val movieId: Int,
    @ColumnInfo(name = "movie_name") val movieName: String,
    @ColumnInfo(name = "movie_description") val movieDescription : String,
    @ColumnInfo(name = "movie_poster_url") val moviePosterUrl : String,
    @ColumnInfo(name = "movie_release_date") val movieReleaseDate : Long
)