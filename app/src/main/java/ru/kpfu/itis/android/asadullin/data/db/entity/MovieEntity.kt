package ru.kpfu.itis.android.asadullin.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kpfu.itis.android.asadullin.model.MovieModel

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "movie_id") val movieId: Int = 0,
    @ColumnInfo(name = "movie_title") val movieTitle: String,
    @ColumnInfo(name = "movie_description") val movieDescription : String,
    @ColumnInfo(name = "movie_poster_url") val moviePosterUrl : String,
    @ColumnInfo(name = "movie_release_year") val movieReleaseYear : Int
) {
    companion object {
        fun fromMovieModel(movieModel: MovieModel) = MovieEntity(
            movieTitle = movieModel.movieTitle,
            movieDescription = movieModel.movieDescription,
            moviePosterUrl = movieModel.moviePosterUrl,
            movieReleaseYear = movieModel.movieReleaseYear
        )
    }
}