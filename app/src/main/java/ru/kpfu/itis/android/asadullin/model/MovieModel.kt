package ru.kpfu.itis.android.asadullin.model

import ru.kpfu.itis.android.asadullin.data.db.entity.MovieEntity

data class MovieModel(
    val movieId: Int? = null,
    val movieTitle: String,
    val movieDescription : String,
    val moviePosterUrl : String,
    val movieReleaseYear : Int
) {
    companion object {
        fun fromMovieEntity(movieEntity: MovieEntity) = MovieModel(
            movieId = movieEntity.movieId,
            movieTitle = movieEntity.movieTitle,
            movieDescription = movieEntity.movieDescription,
            moviePosterUrl = movieEntity.moviePosterUrl,
            movieReleaseYear = movieEntity.movieReleaseYear
        )
    }
}