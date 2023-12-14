package ru.kpfu.itis.android.asadullin.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import ru.kpfu.itis.android.asadullin.R
import ru.kpfu.itis.android.asadullin.databinding.ItemMovieCvBinding
import ru.kpfu.itis.android.asadullin.model.MovieModel
import ru.kpfu.itis.android.asadullin.ui.holders.MovieItem

class MovieAdapter(
    private val glide: RequestManager,
    private val fragmentManager: FragmentManager,
    private val onMovieClicked: ((MovieModel) -> Unit),
    private val root : View,
    private val activity : Activity
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
            onMovieClicked = onMovieClicked
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
}