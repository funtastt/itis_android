package ru.kpfu.itis.android.asadullin.ui.holders

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import ru.kpfu.itis.android.asadullin.R
import ru.kpfu.itis.android.asadullin.databinding.ItemKittensCvBinding
import ru.kpfu.itis.android.asadullin.model.KittenModel


class KittenItem(
    private val viewBinding: ItemKittensCvBinding,
    private val glide: RequestManager,
    private val onNewsClicked: ((KittenModel.KittenData) -> Unit),
    private val onFavouredClicked: ((Int, KittenModel.KittenData) -> Unit),
) : RecyclerView.ViewHolder(viewBinding.root) {
    private val options: RequestOptions = RequestOptions().fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL)
    private var kittenItem: KittenModel.KittenData? = null

    init {
        viewBinding.root.setOnClickListener {
            this.kittenItem?.let(onNewsClicked)
        }
        viewBinding.bookmarkBtnIv.setOnClickListener {
            this.kittenItem?.let {
                val data = it.copy(isFavoured = !it.isFavoured)
                onFavouredClicked(adapterPosition, data)
            }
        }
    }

    fun onBind(kittenItem: KittenModel.KittenData) {
        this.kittenItem = kittenItem
        with(viewBinding) {
            tvKittenTitle.text = kittenItem.kittenFactTitle
            kittenItem.kittenFactContent?.let { newsDetailsTv.text = it }
            kittenItem.kittenImageURL?.let {
                glide
                    .load(kittenItem.kittenImageURL)
                    .apply(options)
                    .into(ivKittenImage)
            }
            changeBookmarkBtnStatus(isChecked = kittenItem.isFavoured)
        }
    }

    fun changeBookmarkBtnStatus(isChecked: Boolean) {
        val likeDrawable = if (isChecked) R.drawable.bookmark_checked else R.drawable.bookmark_unchecked
        viewBinding.bookmarkBtnIv.setImageResource(likeDrawable)
    }
}