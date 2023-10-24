package ru.kpfu.itis.android.asadullin.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    var phoneNumber =
                        if (!s.toString().startsWith("8") && !s.toString().startsWith("+7")) {
                            "8$" + s.toString()
                        } else {
                            s.toString()
                        }
                    val formattedPhoneNumber = formatPhoneNumber(phoneNumber)
                    etPhone.removeTextChangedListener(this)
                    etPhone.setText(formattedPhoneNumber)
                    etPhone.setSelection(formattedPhoneNumber.length)
                    etPhone.addTextChangedListener(this)
                    validateFields()
                }
            })

            etQuestionCount.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    validateFields()
                }
            })

            btnNext.setOnClickListener {
                val questionCount = etQuestionCount.text.toString().toInt()

                (activity as MainActivity).replaceFragment(
                    QuizFragment.newInstance(questionCount), QuizFragment.QUIZ_FRAGMENT_TAG, true
                )
            }
        }
    }

    private fun formatPhoneNumber(phoneNumber: String): String {
        var formattedPhone = phoneNumber.filter { it.isDigit() }

        if (formattedPhone.isNotEmpty()) {
            formattedPhone = "+7" + formattedPhone.substring(1)
        }

        if (formattedPhone.length >= 3) {
            formattedPhone = formattedPhone.substring(0, 2) + "(" + formattedPhone.substring(2)
        }

        if (formattedPhone.length >= 7) {
            formattedPhone = formattedPhone.substring(0, 6) + ")" + formattedPhone.substring(6)
        }

        if (formattedPhone.length >= 11) {
            formattedPhone = formattedPhone.substring(0, 10) + "-" + formattedPhone.substring(10)
        }

        if (formattedPhone.length >= 14 && !formattedPhone.endsWith("-")) {
            formattedPhone = formattedPhone.substring(0, 13) + "-" + formattedPhone.substring(13)
        }

        return formattedPhone

    }

    private fun validateFields() {
        val phoneNumber = viewBinding.etPhone.text.toString()
        val questionCountText = viewBinding.etQuestionCount.text.toString()
        val isValidPhoneNumber = validatePhoneNumber(phoneNumber)
        val isValidQuestionCount = validateQuestionCount(questionCountText)
        viewBinding.btnNext.isEnabled = isValidPhoneNumber && isValidQuestionCount
    }

    private fun validateQuestionCount(questionCountText: String): Boolean {
        val ans =
            questionCountText.isNotEmpty() && (minQuestionCount <= questionCountText.toInt()) && (questionCountText.toInt() <= maxQuestionCount)
        if (!ans) {
            viewBinding.etQuestionCount.error = "Invalid question number"
        }
        return ans
    }

    private fun validatePhoneNumber(phoneNumber: String): Boolean {
        val regex = """^\+\d{1,2}\(\d{3}\)\d{3}-\d{2}-\d{2}$""".toRegex()
        val ans = regex.matches(phoneNumber)

        if (!ans) {
            viewBinding.etPhone.error = "Invalid phone number"
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