package ru.kpfu.itis.android.asadullin.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.android.asadullin.R
import ru.kpfu.itis.android.asadullin.databinding.ItemCatalogHeadingBinding
import ru.kpfu.itis.android.asadullin.databinding.ItemFavoritesBinding
import ru.kpfu.itis.android.asadullin.databinding.ItemMovieCvBinding
import ru.kpfu.itis.android.asadullin.di.ServiceLocator
import ru.kpfu.itis.android.asadullin.model.MovieCatalog
import ru.kpfu.itis.android.asadullin.ui.holders.FavoritesItem
import ru.kpfu.itis.android.asadullin.ui.holders.HeadingItem
import ru.kpfu.itis.android.asadullin.ui.holders.MovieItem
import ru.kpfu.itis.android.asadullin.utils.listeners.OnDeleteClickListener

class MovieAdapter(
    private val glide: RequestManager,
    private val onMovieClicked: ((MovieCatalog.MovieModel) -> Unit),
    private val root: View,
    private val lifecycleScope: LifecycleCoroutineScope,
    private val context: Context,
    private val userId: Int,
    private val onEmptyLibrary: (() -> Unit)
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), OnDeleteClickListener {

    private var moviesCatalog = mutableListOf<MovieCatalog>()
    private var isItemDeleted = false
    private var favoritesItem: FavoritesItem? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.item_movie_cv -> MovieItem(
            binding = ItemMovieCvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            glide = glide,
            onMovieClicked = onMovieClicked,
            lifecycleScope = lifecycleScope
        )

        R.layout.item_catalog_heading -> HeadingItem(
            binding = ItemCatalogHeadingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        R.layout.item_favorites -> FavoritesItem(
            binding = ItemFavoritesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onMovieClicked = onMovieClicked,
            lifecycleScope = lifecycleScope,
            glide = glide,
            context = context,
            userId = userId
        )

        else -> throw RuntimeException("No such view holder...")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieItem -> {
                holder.onBind(moviesCatalog[position] as MovieCatalog.MovieModel)

                if (moviesCatalog.all { it is MovieCatalog.MovieModel }) {
                    holder.setOnDeleteClickListener(this)
                }
            }

            is HeadingItem -> {
                holder.onBind(moviesCatalog[position] as MovieCatalog.CatalogHeading)
            }

            is FavoritesItem -> {
                holder.onBind()
                favoritesItem = holder
            }
        }
    }

    override fun getItemCount(): Int = moviesCatalog.size

    override fun getItemViewType(position: Int): Int {
        return when (moviesCatalog[position]) {
            is MovieCatalog.MovieModel -> R.layout.item_movie_cv
            is MovieCatalog.CatalogHeading -> R.layout.item_catalog_heading
            is MovieCatalog.FavoritesContainer -> R.layout.item_favorites
        }
    }

    fun setItems(list: List<MovieCatalog>) {
        moviesCatalog.clear()
        moviesCatalog.addAll(list)
    }

    fun removeItem(position: Int) {
        val item = moviesCatalog[position] as MovieCatalog.MovieModel
        val snackbar = Snackbar.make(
            root,
            root.context.getString(R.string.item_was_removed_successfully),
            Snackbar.LENGTH_LONG
        )
        isItemDeleted = true
        snackbar.setAction(root.context.getString(R.string.undo)) {
            restoreItem(item, position)
        }

        lifecycleScope.launch(Dispatchers.IO) {
            delay(2000)
            if (isItemDeleted) {
                val movieToDelete = item.movieId

                val movieDao = ServiceLocator.getDatabaseInstance().movieDao
                if (movieToDelete != null) {
                    movieDao.deleteMovieById(movieToDelete)
                }

                withContext(Dispatchers.Main) {
                    favoritesItem?.deleteMovieById(movieToDelete)
                    notifyItemChanged(moviesCatalog.indexOfFirst {it is MovieCatalog.FavoritesContainer})

                    if (favoritesItem?.isListEmpty() == true) {
                        moviesCatalog.removeAt(0)
                        notifyItemRemoved(0)
                        moviesCatalog.removeAt(0)
                        notifyItemRemoved(0)
                    }
                    if (moviesCatalog.size == 1) {
                        moviesCatalog.removeAt(0)
                        notifyItemRemoved(0)
                        onEmptyLibrary()
                    }
                }
            }
        }

        snackbar.setActionTextColor(Color.YELLOW)
        snackbar.show()

        moviesCatalog.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun restoreItem(item: MovieCatalog.MovieModel, position: Int) {
        isItemDeleted = false
        moviesCatalog.add(position, item)
        notifyItemInserted(position)
    }

    override fun onDeleteClick(position: Int) {
        removeItemFromFavorites(position)
    }

    private fun removeItemFromFavorites(position: Int) {
        val item = moviesCatalog[position] as MovieCatalog.MovieModel
        val snackbar = Snackbar.make(
            root,
            root.context.getString(R.string.item_was_removed_successfully),
            Snackbar.LENGTH_SHORT
        )
        snackbar.setAction(root.context.getString(R.string.undo)) {
            restoreItemFromFavorites(item, position)
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val interactionDao = ServiceLocator.getDatabaseInstance().interactionDao
            interactionDao.updateMovieFavored(userId, item.movieId ?: -1, false)
        }

        snackbar.setActionTextColor(Color.YELLOW)
        snackbar.show()

        moviesCatalog.removeAt(position)
        notifyItemRemoved(position)
    }


    private fun restoreItemFromFavorites(item : MovieCatalog.MovieModel, position : Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val interactionDao = ServiceLocator.getDatabaseInstance().interactionDao
            interactionDao.updateMovieFavored(userId, item.movieId ?: -1, true)
        }

        moviesCatalog.add(position, item)
        notifyItemInserted(position)
    }
}