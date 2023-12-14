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
import ru.kpfu.itis.android.asadullin.R
import ru.kpfu.itis.android.asadullin.databinding.ItemMovieCvBinding
import ru.kpfu.itis.android.asadullin.model.MovieModel
import ru.kpfu.itis.android.asadullin.ui.holders.MovieItem

class MovieAdapter(
    private val glide: RequestManager,
    private val fragmentManager: FragmentManager,
    private val onMovieClicked: ((MovieModel) -> Unit),
    private val root : View,
    private val activity : Activity,
    private val lifecycleScope : LifecycleCoroutineScope
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var moviesCatalog = mutableListOf<MovieModel>()

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

        else -> throw RuntimeException("No such view holder...")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieItem -> {
                holder.onBind(moviesCatalog[position] as MovieModel)
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
            is MovieModel -> R.layout.item_movie_cv
            else -> {throw RuntimeException("Error in getItemViewType()")}
        }
    }

//    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<MovieModel>) {
//        val diff = NewsDiffUtil(oldItemsList = moviesCatalog, newItemsList = list)
//        val diffResult = DiffUtil.calculateDiff(diff)
        moviesCatalog.clear()
        moviesCatalog.addAll(list)
//        diffResult.dispatchUpdatesTo(this)
    }

    fun removeItem(position: Int) {
        val item = moviesCatalog[position]
        val snackbar = Snackbar.make(root, root.context.getString(R.string.item_was_removed_successfully), Snackbar.LENGTH_LONG)
        snackbar.setAction(root.context.getString(R.string.undo)) {
            restoreItem(item, position)
        }

        snackbar.setActionTextColor(Color.YELLOW)
        snackbar.show()

        moviesCatalog.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun restoreItem(item: MovieModel, position: Int) {
        moviesCatalog.add(position, item)
        notifyItemInserted(position)
    }
}