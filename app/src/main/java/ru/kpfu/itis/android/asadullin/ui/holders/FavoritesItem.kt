package ru.kpfu.itis.android.asadullin.ui.holders

import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bumptech.glide.RequestManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.android.asadullin.adapters.MovieAdapter
import ru.kpfu.itis.android.asadullin.databinding.ItemFavoritesBinding
import ru.kpfu.itis.android.asadullin.di.ServiceLocator
import ru.kpfu.itis.android.asadullin.model.MovieCatalog

class FavoritesItem(
    private val binding: ItemFavoritesBinding,
    private val onMovieClicked: ((MovieCatalog.MovieModel) -> Unit),
    private val lifecycleScope: LifecycleCoroutineScope,
    private val glide: RequestManager,
    private val context: Context,
    private val userId: Int
) : RecyclerView.ViewHolder(binding.root) {
    private var rv: RecyclerView? = null
    private var moviesAdapter: MovieAdapter? = null
    private var movieList = mutableListOf<MovieCatalog.MovieModel>()

    fun onBind() {
        lifecycleScope.launch(Dispatchers.IO) {
            val favorites =
                ServiceLocator.getDatabaseInstance().interactionDao.getFavoritesByUserId(userId)

            movieList.addAll(ServiceLocator.getDatabaseInstance().movieDao.getFavorites(favorites)
                .map { entity -> MovieCatalog.MovieModel.fromMovieEntity(entity) })

            withContext(Dispatchers.Main) {

                with(binding) {
                    rv = rvFavorites

                    moviesAdapter = MovieAdapter(
                        glide = glide,
                        onMovieClicked = onMovieClicked,
                        root = root,
                        lifecycleScope = lifecycleScope,
                        context = context,
                        userId = userId,
                        onEmptyLibrary = { },
                        onEmptyFavorites = { }
                    )

                    moviesAdapter?.setItems(movieList)

                    val layoutManager: LayoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                    rv?.layoutManager = layoutManager
                    rv?.adapter = moviesAdapter
                }
            }
        }
    }

    private fun onEmptyLibrary() {
    }

    fun deleteMovieById(movieId: Int?) {
        val position = movieList.indexOfFirst {
            it.movieId == movieId
        }

        if (position != -1) {
            movieList.removeAt(position)
            moviesAdapter?.notifyItemRemoved(position)
        }
    }

    fun isListEmpty(): Boolean {
        return movieList.isEmpty()
    }
}