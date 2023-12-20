package ru.kpfu.itis.android.asadullin.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kpfu.itis.android.asadullin.data.db.entity.UserMovieInteractionEntity

@Dao
interface UserMovieInteractionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertInteractionModel(interactionEntity: UserMovieInteractionEntity)

    @Query("SELECT * from user_movie_interactions WHERE movie_id = :movieId AND user_id = :userId")
    fun getInteractionModelById(movieId : Int, userId : Int) : UserMovieInteractionEntity?

    @Query("UPDATE user_movie_interactions SET rating = :rating WHERE user_id = :userId AND movie_id = :movieId")
    fun updateMovieRating(userId: Int, movieId: Int, rating: Int)

    @Query("SELECT rating from user_movie_interactions WHERE movie_id = :movieId")
    fun getRatingByMovieId(movieId : Int) : List<Int>?

    @Query("UPDATE user_movie_interactions SET is_favoured = :isFavored WHERE user_id = :userId AND movie_id = :movieId")
    fun updateMovieFavored(userId: Int, movieId: Int, isFavored: Boolean)

    @Query("SELECT movie_id from user_movie_interactions WHERE user_id = :userId AND is_favoured = 1")
    fun getFavoritesByUserId(userId: Int): List<Int>

    @Query("SELECT * from user_movie_interactions WHERE user_id = :userId")
    fun getRatingsByUserId(userId: Int): List<UserMovieInteractionEntity>
}