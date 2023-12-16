package ru.kpfu.itis.android.asadullin.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.android.asadullin.R
import ru.kpfu.itis.android.asadullin.adapters.MovieAdapter
import ru.kpfu.itis.android.asadullin.data.db.entity.UserMovieInteractionEntity
import ru.kpfu.itis.android.asadullin.databinding.FragmentMoviesCatalogBinding
import ru.kpfu.itis.android.asadullin.di.ServiceLocator
import ru.kpfu.itis.android.asadullin.model.MovieCatalog
import ru.kpfu.itis.android.asadullin.utils.callbacks.HorizontalSwipeToDeleteCallback

class MoviesCatalogFragment : Fragment(R.layout.fragment_movies_catalog) {
    private var _binding: FragmentMoviesCatalogBinding? = null
    private val binding: FragmentMoviesCatalogBinding get() = _binding!!

    private val sharedPreferences by lazy {
        requireActivity().getSharedPreferences("ApplicationPreferences", Context.MODE_PRIVATE)
    }

    private var rv: RecyclerView? = null
    private var moviesAdapter: MovieAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesCatalogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        val glide = Glide.with(this)

        lifecycleScope.launch(Dispatchers.IO) {
            var movieList = mutableListOf<MovieCatalog>()
            var favorites =
                ServiceLocator.getDatabaseInstance().interactionDao.getFavoritesByUserId(getUserId())

            if (favorites.isNotEmpty()) {
                movieList.add(MovieCatalog.CatalogHeading("Favourites"))
                movieList.add(MovieCatalog.FavoritesContainer)
            }

            val moviesLibrary = ServiceLocator.getDatabaseInstance().movieDao
                .getAllFilms().map { entity -> MovieCatalog.MovieModel.fromMovieEntity(entity) }

            if (moviesLibrary.isNotEmpty()) {
                movieList.add(MovieCatalog.CatalogHeading("Library"))
                movieList.addAll(moviesLibrary)
            }

            withContext(Dispatchers.Main) {

                with(binding) {
                    if (movieList.isEmpty()) {
                        tvNoMoviesToShow.visibility = View.VISIBLE
                        return@with
                    }
                    rv = rvMovies

                    moviesAdapter = MovieAdapter(
                        glide = glide,
                        onMovieClicked = ::onMovieClicked,
                        root = requireView(),
                        lifecycleScope = lifecycleScope,
                        context = requireContext(),
                        userId = getUserId(),
                        onEmptyLibrary = {
                            binding.tvNoMoviesToShow.visibility = View.VISIBLE
                        },
                        onEmptyFavorites = {
                            movieList.remove(MovieCatalog.CatalogHeading("Favourites"))
                            moviesAdapter?.setItems(movieList)
                        }
                    )

                    moviesAdapter?.setItems(movieList)

                    val gridLayoutManager = GridLayoutManager(
                        requireContext(),
                        2,
                        GridLayoutManager.VERTICAL,
                        false
                    )

                    gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int) =
                            if (movieList[position] is MovieCatalog.MovieModel) 1
                            else 2
                    }

                    rv?.layoutManager = gridLayoutManager
                    rv?.adapter = moviesAdapter

                    enableSwipeToDelete()
                }
            }
        }
    }

    private fun handleSortSelection(selectedSortType: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val movieDao = ServiceLocator.getDatabaseInstance().movieDao
            val interactionDao = ServiceLocator.getDatabaseInstance().interactionDao

            val moviesLibrary = movieDao.getAllFilms()
                .map { entity -> MovieCatalog.MovieModel.fromMovieEntity(entity) }
            val sortedMovies = mutableListOf<MovieCatalog.MovieModel>()
            var catalog = mutableListOf<MovieCatalog>()

            when (selectedSortType) {
                "By release year (descending)" -> {
                    sortedMovies.addAll(moviesLibrary.sortedByDescending { it.movieReleaseYear })
                }

                "By release year (ascending)" -> {
                    sortedMovies.addAll(moviesLibrary.sortedByDescending { it.movieReleaseYear }
                        .reversed())
                }

                "Your ratings (descending)" -> {
                    val userRatings: List<UserMovieInteractionEntity> =
                        interactionDao.getRatingsByUserId(getUserId()).map {
                            it.copy(rating = it.rating ?: 0)
                        }.sortedByDescending { it.rating }
                    println(userRatings)

                    for (movie in userRatings) {
                        val position = moviesLibrary.indexOfFirst { it.movieId == movie.movieId }
                        sortedMovies.add(moviesLibrary[position])
                    }
                }

                "Your ratings (ascending)" -> {
                    val userRatings: List<UserMovieInteractionEntity> =
                        interactionDao.getRatingsByUserId(getUserId()).map {
                            it.copy(rating = it.rating ?: 0)
                        }.sortedByDescending { it.rating }.reversed()

                    for (movie in userRatings) {
                        val position = moviesLibrary.indexOfFirst { it.movieId == movie.movieId }
                        sortedMovies.add(moviesLibrary[position])
                    }
                }
            }

            var favorites = interactionDao.getFavoritesByUserId(getUserId())

            withContext(Dispatchers.Main) {
                if (favorites.isNotEmpty()) {
                    catalog.add(MovieCatalog.CatalogHeading("Favourites"))
                    catalog.add(MovieCatalog.FavoritesContainer)
                }

                if (moviesLibrary.isNotEmpty()) {
                    catalog.add(MovieCatalog.CatalogHeading("Library"))
                    catalog.addAll(sortedMovies)
                }

                moviesAdapter?.setItems(catalog)
            }
        }

    }

    private fun enableSwipeToDelete() {
        val horizontalSwipeToDeleteCallback = object : HorizontalSwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                val position = viewHolder.adapterPosition

                moviesAdapter?.removeItem(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(horizontalSwipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(rv)
    }

    private fun onMovieClicked(movieModel: MovieCatalog.MovieModel) {
        findNavController().navigate(
            R.id.action_moviesCatalogFragment_to_movieItemFragment,
            bundleOf("movieId" to movieModel.movieId)
        )
    }

    private fun getUserId() = sharedPreferences.getInt("userId", -1)


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}