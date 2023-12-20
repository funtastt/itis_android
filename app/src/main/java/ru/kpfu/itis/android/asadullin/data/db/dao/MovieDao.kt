package ru.kpfu.itis.android.asadullin.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kpfu.itis.android.asadullin.data.db.entity.MovieEntity

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertMovieModel(movieEntity: MovieEntity)

    @Query("SELECT * FROM movies WHERE movie_title = :movieTitle AND movie_release_year = :movieReleaseYear")
    fun getFilmsByTitleAndYear(movieTitle: String, movieReleaseYear: Int): List<MovieEntity>
    @Query("SELECT * FROM movies ORDER BY movie_release_year DESC")
    fun getAllFilms(): List<MovieEntity>

    @Query("SELECT * FROM movies WHERE movie_id = :movieId")
    fun getFilmById(movieId: Int?): MovieEntity

    @Query("DELETE from movies WHERE movie_id = :movieId")
    fun deleteMovieById(movieId: Int)

    @Query("SELECT * FROM movies WHERE movie_id IN (:favorites) ORDER BY movie_release_year DESC")

    fun getFavorites(favorites: List<Int>): List<MovieEntity>
}