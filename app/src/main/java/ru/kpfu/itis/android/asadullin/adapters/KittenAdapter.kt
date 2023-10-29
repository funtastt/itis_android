package ru.kpfu.itis.android.asadullin.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import ru.kpfu.itis.android.asadullin.R
import ru.kpfu.itis.android.asadullin.adapters.diffutil.NewsDiffUtil
import ru.kpfu.itis.android.asadullin.databinding.ItemKittensBsdBtnBinding
import ru.kpfu.itis.android.asadullin.databinding.ItemKittensCvBinding
import ru.kpfu.itis.android.asadullin.databinding.ItemKittensDateBinding
import ru.kpfu.itis.android.asadullin.model.KittenModel
import ru.kpfu.itis.android.asadullin.ui.holders.ButtonItem
import ru.kpfu.itis.android.asadullin.ui.holders.DateItem
import ru.kpfu.itis.android.asadullin.ui.holders.KittenItem
import java.lang.RuntimeException
import java.util.Date

class KittensAdapter(
    private val glide: RequestManager,
    private val fragmentManager: FragmentManager,
    private val onKittenClicked: ((KittenModel.KittenData) -> Unit),
    private val onBookmarkClicked: ((Int, KittenModel.KittenData) -> Unit),
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var kittensList = mutableListOf<KittenModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.item_kittens_cv -> KittenItem(
            viewBinding = ItemKittensCvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            glide = glide,
            onNewsClicked = onKittenClicked,
            onFavouredClicked = onBookmarkClicked,
        )

        R.layout.item_kittens_date -> DateItem(
            viewBinding = ItemKittensDateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        R.layout.item_kittens_bsd_btn -> ButtonItem(
            viewBinding = ItemKittensBsdBtnBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            this,
            fragmentManager
        )

        else -> throw RuntimeException("No such view holder...")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is KittenItem -> holder.onBind(kittensList[position] as KittenModel.KittenData)
            is ButtonItem -> holder.onBind()
            is DateItem -> holder.onBind(Date())
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            (payloads.first() as? Boolean)?.let {
                (holder as? KittenItem)?.changeBookmarkBtnStatus(it)
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemCount(): Int = kittensList.size

    override fun getItemViewType(position: Int): Int {
        return when (kittensList[position]) {
            is KittenModel.KittenButton -> R.layout.item_kittens_bsd_btn
            is KittenModel.KittenData -> R.layout.item_kittens_cv
            is KittenModel.KittenCurrentDate -> R.layout.item_kittens_date
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(list: List<KittenModel>) {
        val diff = NewsDiffUtil(oldItemsList = kittensList, newItemsList = list)
        val diffResult = DiffUtil.calculateDiff(diff)
        kittensList.clear()
        kittensList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateItem(position: Int, item: KittenModel.KittenData) {
        this.kittensList[position] = item
        notifyItemChanged(position, item.isFavoured)
    }
}