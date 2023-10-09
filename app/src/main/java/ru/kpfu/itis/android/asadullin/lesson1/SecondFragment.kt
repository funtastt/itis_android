package ru.kpfu.itis.android.asadullin.lesson1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.kpfu.itis.android.asadullin.lesson1.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {
    private var _viewBinding: FragmentSecondBinding? = null
    private val viewBinding: FragmentSecondBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentSecondBinding.inflate(inflater, container, false)
        val view = viewBinding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        val text = arguments?.getString(ParamsKey.MESSAGE_TEXT_KEY) ?: ""

        with(viewBinding) {
            if (text.isNotEmpty()) {
                tvSecond.text = text
            } else {
                tvSecond.text = "Второй экран"
            }

            btnToThird.setOnClickListener {
                parentFragmentManager.popBackStack()

                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        ThirdFragment.newInstance(text),
                        ThirdFragment.THIRD_FRAGMENT_TAG
                    )
                    .addToBackStack(null)
                    .commit()
            }

            btnToFirst.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    companion object {
        const val SECOND_FRAGMENT_TAG = "SECOND_FRAGMENT_TAG"

        fun newInstance(text: String) = SecondFragment().apply {
            arguments = Bundle().apply {
                putString(ParamsKey.MESSAGE_TEXT_KEY, text)
            }
        }
    }
}
