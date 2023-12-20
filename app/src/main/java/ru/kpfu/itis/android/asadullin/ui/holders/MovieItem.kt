package ru.kpfu.itis.android.asadullin.ui.holders

import android.graphics.Color
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.android.asadullin.R
import ru.kpfu.itis.android.asadullin.adapters.MovieAdapter
import ru.kpfu.itis.android.asadullin.data.db.dao.UserMovieInteractionDao
import ru.kpfu.itis.android.asadullin.databinding.ItemMovieCvBinding
import ru.kpfu.itis.android.asadullin.di.ServiceLocator
import ru.kpfu.itis.android.asadullin.model.MovieCatalog
import ru.kpfu.itis.android.asadullin.utils.listeners.OnDeleteClickListener
import kotlin.math.roundToInt

class MovieItem(
    val binding: ItemMovieCvBinding,
    private val glide: RequestManager,
    private val onMovieClicked: ((MovieCatalog.MovieModel) -> Unit),
    private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.ViewHolder(binding.root) {
    private val options: RequestOptions = RequestOptions().fitCenter().diskCacheStrategy(
        DiskCacheStrategy.ALL
    )
    private var movieItem: MovieCatalog.MovieModel? = null
    private var interactionDao: UserMovieInteractionDao? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null

    init {
        with(binding) {
            root.setOnClickListener {
                movieItem?.let(onMovieClicked)
            }

            root.setOnLongClickListener {
                onDeleteClickListener?.onDeleteClick(adapterPosition)
                true
            }
        }
    }

    fun onBind(movieItem: MovieCatalog.MovieModel) {
        this.movieItem = movieItem

        interactionDao =
            ServiceLocator.getDatabaseInstance().interactionDao

        lifecycleScope.launch(Dispatchers.IO) {
            val ratings: List<Int?>? = interactionDao?.getRatingByMovieId(movieItem.movieId ?: -1)

            val avgRating: Double
            if (ratings.isNullOrEmpty()) {
                avgRating = 0.0
            } else {
                var size = 0
                var sum = 0
                for (rating in ratings) {
                    if (rating != null) {
                        size++
                        sum += rating
                    }
                }
                avgRating = if (size == 0) 0.0
                else (sum * 10.0 / size).roundToInt() / 10.0
            }

            withContext(Dispatchers.Main) {
                with(binding) {
                    glide.load(movieItem.moviePosterUrl)
                        .apply(options)
                        .error(R.drawable.error)
                        .into(ivCardViewPoster)

                    tvCardViewTitle.text = movieItem.movieTitle
                    tvCardViewYear.text = movieItem.movieReleaseYear.toString()
                    tvCardViewRating.text = avgRating.toString()

                    if (avgRating == 0.0) {
                        vCardViewBackground.setBackgroundColor(Color.rgb(118, 120, 119))
                    } else if (avgRating < 2.5) {
                        vCardViewBackground.setBackgroundColor(Color.rgb(182, 21, 11))
                    } else if (avgRating < 4.2) {
                        vCardViewBackground.setBackgroundColor(Color.rgb(251, 174, 68))
                    } else {
                        vCardViewBackground.setBackgroundColor(Color.rgb(55, 182, 53))
                    }

                }
            }
        }
    }

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }
}