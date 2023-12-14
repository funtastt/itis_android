package ru.kpfu.itis.android.asadullin.ui.holders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import ru.kpfu.itis.android.asadullin.R
import ru.kpfu.itis.android.asadullin.databinding.ItemMovieCvBinding
import ru.kpfu.itis.android.asadullin.model.MovieModel

class MovieItem(
    val binding: ItemMovieCvBinding,
    private val glide: RequestManager,
    private val onMovieClicked: ((MovieModel) -> Unit),
) : RecyclerView.ViewHolder(binding.root) {
    private val options: RequestOptions = RequestOptions().fitCenter().diskCacheStrategy(
        DiskCacheStrategy.ALL)
    private var movieItem: MovieModel? = null

    init {
        with(binding) {
            root.setOnClickListener {
                movieItem?.let(onMovieClicked)
            }
        }
    }

    fun onBind(movieItem: MovieModel) {
        this.movieItem = movieItem

        with(binding) {
            glide.
                load(movieItem.moviePosterUrl)
                .apply(options)
                .error(R.drawable.error)
                .into(ivCardViewPoster)

            tvCardViewTitle.text = movieItem.movieTitle
            tvCardViewYear.text = movieItem.movieReleaseYear.toString()
        }
    }
}