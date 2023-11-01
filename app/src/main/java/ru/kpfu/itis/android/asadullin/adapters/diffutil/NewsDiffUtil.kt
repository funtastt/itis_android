package ru.kpfu.itis.android.asadullin.adapters.diffutil

import androidx.recyclerview.widget.DiffUtil
import ru.kpfu.itis.android.asadullin.model.KittenModel

class NewsDiffUtil(
    private val oldItemsList: List<KittenModel>,
    private val newItemsList: List<KittenModel>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItemsList.size

    override fun getNewListSize(): Int = newItemsList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]
        if (oldItem !is KittenModel.KittenData || newItem !is KittenModel.KittenData) {
            return false
        }
        return oldItem.kittenId == newItem.kittenId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]
        if (oldItem !is KittenModel.KittenData || newItem !is KittenModel.KittenData) {
            return false
        }
        return (oldItem.kittenFactTitle == newItem.kittenFactTitle) &&
                (oldItem.kittenFactContent == newItem.kittenFactContent)
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldItemsList[oldItemPosition]
        val newItem = newItemsList[newItemPosition]
        if (oldItem !is KittenModel.KittenData || newItem !is KittenModel.KittenData) {
            return super.getChangePayload(oldItemPosition, newItemPosition)
        }
        return if (oldItem.isFavoured != newItem.isFavoured) {
            newItem.isFavoured
        } else {
            super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }
}