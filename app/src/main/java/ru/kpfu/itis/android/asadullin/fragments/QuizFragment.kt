package ru.kpfu.itis.android.asadullin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import ru.kpfu.itis.android.asadullin.util.ParamsKey
import ru.kpfu.itis.android.asadullin.R
import ru.kpfu.itis.android.asadullin.adapters.QuizFragmentAdapter
import ru.kpfu.itis.android.asadullin.databinding.FragmentQuizBinding
import ru.kpfu.itis.android.asadullin.util.QuestionGenerator

class QuizFragment : Fragment(R.layout.fragment_quiz), AnswerChangedListener {
    private var _viewBinding: FragmentQuizBinding? = null
    private val viewBinding: FragmentQuizBinding get() = _viewBinding!!

    private var answers: HashMap<Int, Int> = hashMapOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentQuizBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        with(viewBinding) {
            val questionsNumber = requireArguments().getInt(ParamsKey.NUMBER_OF_QUESTIONS_KEY)
            val questions = QuestionGenerator.generateQuestions(questionsNumber)
            val vpQuestionsAdapter = QuizFragmentAdapter(questions, childFragmentManager, lifecycle)

            vpQuestions.apply {
                adapter = vpQuestionsAdapter

                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        val realPos = when (position) {
                            0 -> questionsNumber
                            questionsNumber + 1 -> 1
                            else -> position
                        }
                        tvHeader.text = "Question number $realPos out of $questionsNumber"
                    }

                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                        if (position == questionsNumber + 1 && positionOffset == 0.0f) {
                            setCurrentItem(1, false)
                        } else if (position == 0 && positionOffset == 0.0f) {
                            setCurrentItem(questionsNumber, false)
                        }
                    }
                })
                setCurrentItem(1, false)
            }

            btnFinish.setOnClickListener {
                showToast("Good job!")
            }
        }
    }

    override fun onDestroy() {
        _viewBinding = null
        super.onDestroy()
    }

    companion object {
        const val QUIZ_FRAGMENT_TAG = "QUIZ_FRAGMENT_TAG"
        fun newInstance(questionsNumber: Int) = QuizFragment().apply {
            arguments = bundleOf(ParamsKey.NUMBER_OF_QUESTIONS_KEY to questionsNumber)
        }
    }

    override fun onAnswerChanged(questionPosition: Int, answerPosition: Int) {
        answers[questionPosition] = answerPosition
        val numberOfQuestions = requireArguments().getInt(ParamsKey.NUMBER_OF_QUESTIONS_KEY)
        if (answers.count() == numberOfQuestions) {
            viewBinding.btnFinish.visibility = View.VISIBLE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}