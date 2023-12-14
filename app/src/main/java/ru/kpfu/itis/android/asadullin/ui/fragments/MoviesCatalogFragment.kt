package ru.kpfu.itis.android.asadullin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.android.asadullin.R
import ru.kpfu.itis.android.asadullin.adapters.MovieAdapter
import ru.kpfu.itis.android.asadullin.databinding.FragmentMoviesCatalogBinding
import ru.kpfu.itis.android.asadullin.di.ServiceLocator
import ru.kpfu.itis.android.asadullin.model.MovieCatalog
import ru.kpfu.itis.android.asadullin.utils.callbacks.SwipeToDeleteCallback

class MoviesCatalogFragment : Fragment(R.layout.fragment_movies_catalog) {
    private var _binding: FragmentMoviesCatalogBinding? = null
    private val binding: FragmentMoviesCatalogBinding get() = _binding!!

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
            movieList.add(MovieCatalog.CatalogHeading("Favourites"))
            movieList.add(MovieCatalog.CatalogHeading("Library"))
            movieList.addAll(ServiceLocator.getDatabaseInstance().movieDao
                .getAllFilms().map { entity -> MovieCatalog.MovieModel.fromMovieEntity(entity) })

            withContext(Dispatchers.Main) {

                with(binding) {
                    if (movieList.isEmpty()) {
                        tvNoMoviesToShow.visibility = View.VISIBLE
                        return@with
                    }
                    rv = rvMovies

                    moviesAdapter = MovieAdapter(
                        glide = glide,
                        fragmentManager = parentFragmentManager,
                        onMovieClicked = ::onMovieClicked,
                        root = requireView(),
                        activity = requireActivity(),
                        lifecycleScope = lifecycleScope
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

    private fun enableSwipeToDelete() {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                val position = viewHolder.adapterPosition

                moviesAdapter?.removeItem(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(rv)
    }

    private fun onMovieClicked(movieModel: MovieCatalog.MovieModel) {
        findNavController().navigate(
            R.id.action_moviesCatalogFragment_to_movieItemFragment,
            bundleOf("movieId" to movieModel.movieId)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}