package ru.kpfu.itis.android.asadullin.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.android.asadullin.R
import ru.kpfu.itis.android.asadullin.data.db.dao.MovieDao
import ru.kpfu.itis.android.asadullin.data.db.dao.UserMovieInteractionDao
import ru.kpfu.itis.android.asadullin.databinding.FragmentMovieItemBinding
import ru.kpfu.itis.android.asadullin.di.ServiceLocator
import ru.kpfu.itis.android.asadullin.model.MovieCatalog
import kotlin.math.roundToInt

class MovieItemFragment : Fragment(R.layout.fragment_movie_item) {
    private var _binding: FragmentMovieItemBinding? = null
    private val binding: FragmentMovieItemBinding get() = _binding!!

    private val options: RequestOptions = RequestOptions().fitCenter().diskCacheStrategy(
        DiskCacheStrategy.ALL
    )

    private val movieDao: MovieDao = ServiceLocator.getDatabaseInstance().movieDao
    private val interactionDao: UserMovieInteractionDao =
        ServiceLocator.getDatabaseInstance().interactionDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieItemBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        val movieId = arguments?.getInt("movieId") ?: -1
        val glide = Glide.with(this)

        lifecycleScope.launch(Dispatchers.IO) {
            val movieModel: MovieCatalog.MovieModel = MovieCatalog.MovieModel.fromMovieEntity(movieDao.getFilmById(movieId))
            updateRating(movieId)

            withContext(Dispatchers.Main) {
                with(binding) {
                    tvMovieItemTitle.text = movieModel.movieTitle
                    tvMovieItemDescription.text = movieModel.movieDescription
                    tvMovieItemReleaseYear.text =
                        getString(R.string.movie_item_release_year, movieModel.movieReleaseYear)


                    glide.load(movieModel.moviePosterUrl)
                        .apply(options)
                        .error(R.drawable.error)
                        .into(ivMovieItemPoster)

                    btnRateMovie.setOnClickListener {
                        val ratingDialogFragment = RatingDialogFragment(
                            movieId,
                            object : RatingDialogFragment.RatingDialogListener {
                                override fun onRatingConfirmed(movieId: Int) {
                                    updateRating(movieId)
                                }
                            })
                        ratingDialogFragment.show(
                            childFragmentManager,
                            RatingDialogFragment::class.java.simpleName
                        )
                    }
                }
            }
        }
    }

    private fun updateRating(movieId: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val ratings: List<Int>? = interactionDao.getRatingByMovieId(movieId)
            val avgRating = if (ratings.isNullOrEmpty()) 0.0
            else (ratings.sum() * 10.0 / ratings.size).roundToInt() / 10.0

            withContext(Dispatchers.Main) {
                with(binding) {
                    tvCardViewRating.text = avgRating.toString()
                    if (avgRating == 0.0) {
                        vItemBackground.setBackgroundColor(Color.rgb(118,120,119))
                    } else if (avgRating < 2.5) {
                        vItemBackground.setBackgroundColor(Color.rgb(182,21,11))
                    } else if (avgRating < 4.2) {
                        vItemBackground.setBackgroundColor(Color.rgb(251,174,68))
                    } else {
                        vItemBackground.setBackgroundColor(Color.rgb(55,182,53))
                    }
                }
            }
        }
    }
}