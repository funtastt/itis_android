package ru.kpfu.itis.android.asadullin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.kpfu.itis.android.asadullin.adapters.AnswerAdapter
import ru.kpfu.itis.android.asadullin.databinding.FragmentQuestionBinding
import ru.kpfu.itis.android.asadullin.model.Question
import ru.kpfu.itis.android.asadullin.util.ParamsKey

class QuestionFragment : Fragment() {
    private var _viewBinding: FragmentQuestionBinding? = null
    private val viewBinding: FragmentQuestionBinding get() = _viewBinding!!

    private var answerAdapter: AnswerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentQuestionBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        val question = requireArguments().getSerializable(ParamsKey.QUESTION_KEY) as Question

        with(viewBinding) {
            tvQuestion.text = question.question
            answerAdapter = AnswerAdapter(
                answers = question.answersList.toMutableList(),
                onItemChecked = { position ->
                    updateAnswer(position)
                },
                onRootClicked = { position ->
                    updateAnswer(position)
                }
            )
            rvAnswers.adapter = answerAdapter
        }
    }

    private fun updateAnswer(position: Int) {
        answerAdapter?.apply {
            answers.forEach { answer ->
                answer.checked = false
            }

            answers.getOrNull(position)?.checked = true
            notifyDataSetChanged()

            val listener: AnswerChangedListener =
                requireActivity()
                    .supportFragmentManager
                    .findFragmentByTag(QuizFragment.QUIZ_FRAGMENT_TAG)
                        as AnswerChangedListener

            listener.onAnswerChanged(
                requireArguments().getInt(ParamsKey.QUESTION_POSITION_KEY),
                position
            )
        }
    }

    override fun onDestroy() {
        _viewBinding = null
        super.onDestroy()
    }

    companion object {
        const val QUESTION_FRAGMENT_TAG = "QUESTION_FRAGMENT_TAG"

        fun newInstance(questionPosition: Int, question: Question) = QuestionFragment().apply {
            arguments = Bundle().apply {
                putInt(ParamsKey.QUESTION_POSITION_KEY, questionPosition)
                putSerializable(ParamsKey.QUESTION_KEY, question)
            }
        }
    }
}
