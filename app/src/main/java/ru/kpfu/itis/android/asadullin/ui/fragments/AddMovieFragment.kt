package ru.kpfu.itis.android.asadullin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.HttpException
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.android.asadullin.R
import ru.kpfu.itis.android.asadullin.data.db.dao.MovieDao
import ru.kpfu.itis.android.asadullin.data.db.entity.MovieEntity
import ru.kpfu.itis.android.asadullin.databinding.FragmentAddMovieBinding
import ru.kpfu.itis.android.asadullin.di.ServiceLocator
import ru.kpfu.itis.android.asadullin.model.MovieCatalog
import java.io.FileNotFoundException
import java.util.Calendar

class AddMovieFragment : Fragment(R.layout.fragment_add_movie) {
    private var _binding: FragmentAddMovieBinding? = null
    private val binding: FragmentAddMovieBinding get() = _binding!!

    private val movieDao: MovieDao = ServiceLocator.getDatabaseInstance().movieDao

    private val options: RequestOptions = RequestOptions().fitCenter().diskCacheStrategy(
        DiskCacheStrategy.ALL
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMovieBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        val glide = Glide.with(this)

        with(binding) {
            btnAddMovie.isEnabled = false

            etReleaseYear.addTextChangedListener {
                updateButtonState()
            }

            etMovieTitle.addTextChangedListener {
                updateButtonState()
            }

            etDescription.addTextChangedListener {
                updateButtonState()
            }

            etPoster.addTextChangedListener {
                updateButtonState()

                glide
                    .load(etPoster.text.toString())
                    .apply(options)
                    .error(R.drawable.error)
                    .into(ivPoster)
            }

            btnAddMovie.setOnClickListener {
                val newMovie = MovieCatalog.MovieModel(
                    movieTitle = etMovieTitle.text.toString(),
                    movieReleaseYear = etReleaseYear.text.toString().toIntOrNull() ?: 0,
                    movieDescription = etDescription.text.toString(),
                    moviePosterUrl = etPoster.text.toString()
                )

                lifecycleScope.launch(Dispatchers.IO) {
                    val isAlreadyExists = movieDao.getFilmsByTitleAndYear(newMovie.movieTitle, newMovie.movieReleaseYear).isNotEmpty()

                    if (isAlreadyExists) {
                        withContext(Dispatchers.Main) {
                            Snackbar.make(
                                requireView(),
                                getString(R.string.movie_already_exists),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        movieDao.insertMovieModel(MovieEntity.fromMovieModel(newMovie))

                        withContext(Dispatchers.Main) {
                            etMovieTitle.setText("")
                            etReleaseYear.setText("")
                            etDescription.setText("")
                            etPoster.setText("")

                            Snackbar.make(
                                requireView(),
                                getString(R.string.successfully_added_movie),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun updateButtonState() {
        with(binding) {
            val isTitleEmpty = etMovieTitle.text.isNullOrEmpty()
            val isYearEmpty = etReleaseYear.text.isNullOrEmpty()
            val isDescriptionEmpty = etDescription.text.isNullOrEmpty()
            val isPosterEmpty = etPoster.text.isNullOrEmpty()

            val isAnyFieldEmpty = isTitleEmpty || isYearEmpty || isDescriptionEmpty || isPosterEmpty

            val isYearValid =
                etReleaseYear.text.toString().toIntOrNull() in 1895..Calendar.getInstance()
                    .get(Calendar.YEAR)

            val currentDrawable = ivPoster.drawable
            var isPosterValid = false
            if (currentDrawable != null && currentDrawable.constantState != null) {
                val errorDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.error)

                isPosterValid = currentDrawable.constantState != errorDrawable?.constantState
            }
            btnAddMovie.isEnabled = !isAnyFieldEmpty && isYearValid && isPosterValid
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
