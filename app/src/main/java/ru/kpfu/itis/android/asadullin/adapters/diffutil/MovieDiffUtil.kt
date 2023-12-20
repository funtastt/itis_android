package ru.kpfu.itis.android.asadullin.adapters.diffutil

import androidx.recyclerview.widget.DiffUtil
import ru.kpfu.itis.android.asadullin.model.MovieCatalog

class MovieDiffUtil(
    private val oldItemsList: List<MovieCatalog>,
    private val newItemsList: List<MovieCatalog>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItemsList.size

    override fun getNewListSize(): Int = newItemsList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]
        if (oldItem !is MovieCatalog.MovieModel || newItem !is MovieCatalog.MovieModel) {
            return false
        }
        return oldItem.movieId == newItem.movieId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]
        if (oldItem !is MovieCatalog.MovieModel || newItem !is MovieCatalog.MovieModel) {
            return false
        }
        return (oldItem.movieTitle == newItem.movieTitle) &&
                (oldItem.movieDescription == newItem.movieDescription) &&
                (oldItem.movieReleaseYear == newItem.movieReleaseYear) &&
                (oldItem.moviePosterUrl == newItem.moviePosterUrl)
    }
}