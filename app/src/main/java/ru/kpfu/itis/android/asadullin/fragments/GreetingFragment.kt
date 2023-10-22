package ru.kpfu.itis.android.asadullin.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ru.kpfu.itis.android.asadullin.MainActivity
import ru.kpfu.itis.android.asadullin.databinding.FragmentGreetingBinding
import ru.kpfu.itis.android.asadullin.util.CountryRepository

class GreetingFragment : Fragment() {
    private var _viewBinding: FragmentGreetingBinding? = null
    private val viewBinding: FragmentGreetingBinding get() = _viewBinding!!

    private val minQuestionCount = 9
    private val maxQuestionCount = CountryRepository.list.size

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentGreetingBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        with(viewBinding) {
            etPhone.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    validateFields()
                }
            })

            etQuestionCount.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    validateFields()
                }
            })

            btnNext.setOnClickListener {
                val questionCount = etQuestionCount.text.toString().toInt()

                btnNext.setOnClickListener {
                    (activity as MainActivity).replaceFragment(
                        QuizFragment.newInstance(questionCount), QuizFragment.QUIZ_FRAGMENT_TAG, true
                    )
                }
            }
        }
    }

    private fun validateFields() {
        val phoneNumber = viewBinding.etPhone.text.toString()
        val questionCountText = viewBinding.etQuestionCount.text.toString()
        val isValidPhoneNumber = validatePhoneNumber(phoneNumber)
        val isValidQuestionCount = validateQuestionCount(questionCountText)

        viewBinding.btnNext.isEnabled = isValidPhoneNumber && isValidQuestionCount
    }

    private fun validateQuestionCount(questionCountText: String): Boolean {
        val ans = questionCountText.isNotEmpty() && (minQuestionCount <= questionCountText.toInt()) && (questionCountText.toInt() <= maxQuestionCount)
        if (!ans) {
            showSnackBar("Invalid question count...")
        }
        return ans
    }

    private fun validatePhoneNumber(phoneNumber: String): Boolean {
        val regex = """^(?:\+79\d{8}|89\d{9})$""".toRegex()
        val ans = regex.matches(phoneNumber)

        if (!ans) {
            showSnackBar("Invalid phone number...")
        }

        return ans
    }

    private fun showSnackBar(message: String) {
        view?.let { Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show() }
    }

    override fun onDestroyView() {
        _viewBinding = null
        super.onDestroyView()
    }

    companion object {
        const val GREETING_FRAGMENT_TAG = "GREETING_FRAGMENT_TAG"
    }
}