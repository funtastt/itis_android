package ru.kpfu.itis.android.asadullin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.android.asadullin.adapters.holder.AnswerItem
import ru.kpfu.itis.android.asadullin.databinding.ItemAnswerBinding
import ru.kpfu.itis.android.asadullin.model.Answer

class AnswerAdapter(
    val answers: List<Answer>,
    private val onItemChecked: (Int) -> Unit,
    private val onRootClicked: (Int) -> Unit,
) : RecyclerView.Adapter<AnswerItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerItem {
        return AnswerItem(
            ItemAnswerBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onItemChecked,
            onRootClicked,
        )
    }

    override fun getItemCount(): Int = answers.size

    override fun onBindViewHolder(holder: AnswerItem, position: Int) {
        holder.onBind(answers[position])
    }
}