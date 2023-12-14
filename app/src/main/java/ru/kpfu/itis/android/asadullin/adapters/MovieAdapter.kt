package ru.kpfu.itis.android.asadullin.adapters

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.kpfu.itis.android.asadullin.R
import ru.kpfu.itis.android.asadullin.databinding.ItemCatalogHeadingBinding
import ru.kpfu.itis.android.asadullin.databinding.ItemMovieCvBinding
import ru.kpfu.itis.android.asadullin.di.ServiceLocator
import ru.kpfu.itis.android.asadullin.model.MovieCatalog
import ru.kpfu.itis.android.asadullin.ui.holders.HeadingItem
import ru.kpfu.itis.android.asadullin.ui.holders.MovieItem

class MovieAdapter(
    private val glide: RequestManager,
    private val fragmentManager: FragmentManager,
    private val onMovieClicked: ((MovieCatalog.MovieModel) -> Unit),
    private val root: View,
    private val activity: Activity,
    private val lifecycleScope: LifecycleCoroutineScope
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var moviesCatalog = mutableListOf<MovieCatalog>()
    var isItemDeleted = false

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

        else -> throw RuntimeException("No such view holder...")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieItem -> {
                holder.onBind(moviesCatalog[position] as MovieCatalog.MovieModel)
            }
            is HeadingItem -> {
                holder.onBind(moviesCatalog[position] as MovieCatalog.CatalogHeading)
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemCount(): Int = moviesCatalog.size

    override fun getItemViewType(position: Int): Int {
        return when (moviesCatalog[position]) {
            is MovieCatalog.MovieModel -> R.layout.item_movie_cv
            is MovieCatalog.CatalogHeading -> R.layout.item_catalog_heading
        }
    }

    //    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<MovieCatalog>) {
//        val diff = NewsDiffUtil(oldItemsList = moviesCatalog, newItemsList = list)
//        val diffResult = DiffUtil.calculateDiff(diff)
        moviesCatalog.clear()
        moviesCatalog.addAll(list)
//        diffResult.dispatchUpdatesTo(this)
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
            delay(6000)
            if (isItemDeleted) {
                val movieToDelete = item.movieId

                val movieDao = ServiceLocator.getDatabaseInstance().movieDao
                if (movieToDelete != null) {
                    movieDao.deleteMovieById(movieToDelete)
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
}