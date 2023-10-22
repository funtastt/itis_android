package ru.kpfu.itis.android.asadullin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.kpfu.itis.android.asadullin.databinding.FragmentGreetingBinding

class GreetingFragment : Fragment() {
    private var _viewBinding: FragmentGreetingBinding? = null
    private val viewBinding: FragmentGreetingBinding get() = _viewBinding!!

    private val maxQuestionCount = 10

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentGreetingBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                val phoneNumber = etPhone.text.toString()
                val questionCount = etQuestionCount.text.toString().toInt()

                if (!validatePhoneNumber(phoneNumber)) {
                    showToast("Invalid phone number")
                } else if (questionCount > maxQuestionCount) {
                    showToast("Question count exceeds the limit")
                } else {
                }
            }
        }
    }

    private fun validateFields() {
        val phoneNumber = viewBinding.etPhone.text.toString()
        val questionCountText = viewBinding.etQuestionCount.text.toString()
        val isValidPhoneNumber = validatePhoneNumber(phoneNumber)
        val isValidQuestionCount = questionCountText.isNotEmpty() && questionCountText.toInt() <= maxQuestionCount

        viewBinding.btnNext.isEnabled = isValidPhoneNumber && isValidQuestionCount
    }

    private fun validatePhoneNumber(phoneNumber: String): Boolean {
        val regex = """^(?:\+79\d{8}|89\d{9})$""".toRegex()
        return regex.matches(phoneNumber)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        _viewBinding = null
        super.onDestroyView()
    }

    companion object {
        const val GREETING_FRAGMENT_TAG = "GREETING_FRAGMENT_TAG"
    }
}