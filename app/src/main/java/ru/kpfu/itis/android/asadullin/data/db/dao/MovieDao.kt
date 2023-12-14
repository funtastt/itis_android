package ru.kpfu.itis.android.asadullin.data.db.dao

import androidx.room.Dao
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
}