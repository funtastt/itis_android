package ru.kpfu.itis.android.asadullin.ui.holders

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import ru.kpfu.itis.android.asadullin.R
import ru.kpfu.itis.android.asadullin.databinding.ItemKittensCvBinding
import ru.kpfu.itis.android.asadullin.model.KittenModel
import ru.kpfu.itis.android.asadullin.util.listeners.OnDeleteClickListener


class KittenItem(
    val viewBinding: ItemKittensCvBinding,
    private val glide: RequestManager,
    private val onKittenClicked: ((KittenModel.KittenData) -> Unit),
    private val onBookmarkClicked: ((Int, KittenModel.KittenData) -> Unit),
    private val enableDeleteButton : Boolean
) : RecyclerView.ViewHolder(viewBinding.root) {
    private val options: RequestOptions = RequestOptions().fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL)
    private var kittenItem: KittenModel.KittenData? = null
    private var onDeleteClickListener: OnDeleteClickListener? = null

    init {
        with(viewBinding) {
            root.setOnClickListener {
                kittenItem?.let(onKittenClicked)
            }

            if (enableDeleteButton) {
                root.setOnLongClickListener {
                    if (ivDeleteItem.isVisible) {
                        ivDeleteItem.visibility = View.GONE
                    } else {
                        ivDeleteItem.visibility = View.VISIBLE
                    }
                    true
                }

                ivKittenImage.setOnLongClickListener {
                    if (ivDeleteItem.isVisible) {
                        ivDeleteItem.visibility = View.GONE
                    } else {
                        ivDeleteItem.visibility = View.VISIBLE
                    }
                    true
                }
                ivDeleteItem.setOnClickListener {
                    onDeleteClickListener?.onDeleteClick(adapterPosition)
                }
            }

            ivKittenImage.setOnClickListener {
                kittenItem?.let(onKittenClicked)
            }
            bookmarkBtnIv.setOnClickListener {
                kittenItem?.let {
                    val data = it.copy(isFavoured = !it.isFavoured)
                    onBookmarkClicked(adapterPosition, data)
                }
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

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        onDeleteClickListener = listener
    }
}