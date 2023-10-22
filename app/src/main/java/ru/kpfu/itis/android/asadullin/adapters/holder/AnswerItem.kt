package ru.kpfu.itis.android.asadullin.adapters.holder

import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.android.asadullin.databinding.ItemAnswerBinding
import ru.kpfu.itis.android.asadullin.model.Answer


class AnswerItem(
    private val viewBinding: ItemAnswerBinding,
    private val onItemChecked: (Int) -> Unit,
    private val onRootClicked: (Int) -> Unit,
) : RecyclerView.ViewHolder(viewBinding.root) {

    init {
        viewBinding.rbAnswer.setOnClickListener {
            onItemChecked.invoke(adapterPosition)
        }
        viewBinding.root.setOnClickListener {
            onRootClicked.invoke(adapterPosition)
        }
    }

    // Нельзя выбрать один и тот же вариант несколько раз подряд
    fun onBind(answer: Answer) {
        with(viewBinding) {
            tvAnswer.text = answer.answer
            rbAnswer.isChecked = answer.checked
            rbAnswer.isEnabled = !answer.checked
            if (answer.checked) {
                root.foreground = null
            }
        }
    }
}