package ru.kpfu.itis.android.asadullin.adapters.callback

import androidx.recyclerview.widget.DiffUtil
import ru.kpfu.itis.android.asadullin.model.Answer

class AnswersDiffCallback(
    private val oldAnswers: List<Answer>,
    private val newAnswers: List<Answer>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldAnswers[oldItemPosition] == newAnswers[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldAnswers.size
    }

    override fun getNewListSize(): Int {
        return newAnswers.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldAnswers[oldItemPosition] == newAnswers[newItemPosition]
    }
}