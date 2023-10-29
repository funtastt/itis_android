package ru.kpfu.itis.android.asadullin.ui.holders

import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.android.asadullin.databinding.ItemKittensDateBinding
import java.text.SimpleDateFormat
import java.util.Date

class DateItem(
    private val viewBinding: ItemKittensDateBinding,
) : RecyclerView.ViewHolder(viewBinding.root) {
    fun onBind(date : Date) {
        with(viewBinding) {
            tvKittensDate.text = SimpleDateFormat("dd/MM/yyyy").format(date)
        }
    }
}