package ru.kpfu.itis.android.asadullin.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import ru.kpfu.itis.android.asadullin.data.db.dao.UserDao
import ru.kpfu.itis.android.asadullin.databinding.FragmentMovieItemBinding
import ru.kpfu.itis.android.asadullin.di.ServiceLocator
import ru.kpfu.itis.android.asadullin.model.MovieModel

class MovieItemFragment : Fragment(R.layout.fragment_movie_item) {
    private var _binding: FragmentMovieItemBinding? = null
    private val binding: FragmentMovieItemBinding get() = _binding!!

    private val sharedPreferences by lazy {
        requireActivity().getSharedPreferences("ApplicationPreferences", Context.MODE_PRIVATE)
    }

    private val options: RequestOptions = RequestOptions().fitCenter().diskCacheStrategy(
        DiskCacheStrategy.ALL
    )

    private val userDao: UserDao = ServiceLocator.getDatabaseInstance().userDao
    private val movieDao: MovieDao = ServiceLocator.getDatabaseInstance().movieDao
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
        val movieId = arguments?.getInt("movieId")
        val glide = Glide.with(this)

        lifecycleScope.launch(Dispatchers.IO) {
            val movieModel: MovieModel = MovieModel.fromMovieEntity(movieDao.getFilmById(movieId))

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
                        RatingDialogFragment().show(
                            childFragmentManager,
                            RatingDialogFragment::class.java.simpleName
                        )
                    }
                }
            }
        }
    }

    private fun updateRating(movieId: Int?, rating: Float) {
        Toast.makeText(context, rating.toString(), Toast.LENGTH_SHORT).show()
    }
}