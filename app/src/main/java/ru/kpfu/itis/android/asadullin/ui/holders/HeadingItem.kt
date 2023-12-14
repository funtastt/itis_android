package ru.kpfu.itis.android.asadullin.ui.holders

import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.android.asadullin.databinding.ItemCatalogHeadingBinding
import ru.kpfu.itis.android.asadullin.model.MovieCatalog

class HeadingItem(
    private val binding: ItemCatalogHeadingBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun onBind(catalogHeading: MovieCatalog.CatalogHeading) {
        with(binding) {
            tvCatalogHeading.text = catalogHeading.heading
        }
    }
}