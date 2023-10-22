package ru.kpfu.itis.android.asadullin.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.kpfu.itis.android.asadullin.fragments.QuestionFragment
import ru.kpfu.itis.android.asadullin.model.Question

class QuizFragmentAdapter(
    private val questionsList: List<Question>,
    manager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(manager, lifecycle) {

    override fun getItemCount(): Int = questionsList.size + 2

    override fun createFragment(position: Int): Fragment {
        val realPosition = if (position == 0) questionsList.size - 1 else (position - 1) % questionsList.size
        return QuestionFragment.newInstance(realPosition, questionsList[realPosition])
    }
}