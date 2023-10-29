package ru.kpfu.itis.android.asadullin.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.kpfu.itis.android.asadullin.MainActivity
import ru.kpfu.itis.android.asadullin.databinding.FragmentGreetingBinding
import ru.kpfu.itis.android.asadullin.util.KittenFactsRepository

class GreetingFragment : Fragment() {
    private var _viewBinding: FragmentGreetingBinding? = null
    private val viewBinding: FragmentGreetingBinding get() = _viewBinding!!

    private final val MAX_KITTENS_COUNT = 45

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
            etKittensCount.addTextChangedListener(object : TextWatcher {
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
                    val isValid = validateKittensCount(etKittensCount.text.toString())

                    if (!isValid) {
                        etKittensCount.error = "Too many news entered..."
                        btnEnterKittensCount.isEnabled = false
                    } else {
                        btnEnterKittensCount.isEnabled = true
                    }
                }
            })

            btnEnterKittensCount.setOnClickListener {
                val newsCount = etKittensCount.text.toString().toIntOrNull() ?: 0

                KittenFactsRepository.clearCurrentList()

                (activity as MainActivity).replaceFragment(
                    KittensFragment.newInstance(newsCount), KittensFragment.KITTENS_FRAGMENT_TAG, true
                )
            }
        }
    }

    private fun validateKittensCount(newsCount: String): Boolean {
        return (newsCount.toIntOrNull() ?: 0) <= MAX_KITTENS_COUNT
    }

    override fun onDestroy() {
        _viewBinding = null
        super.onDestroy()
    }

    companion object {
        const val GREETING_FRAGMENT_TAG = "GREETING_FRAGMENT_TAG"
    }
}