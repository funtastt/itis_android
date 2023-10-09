package ru.kpfu.itis.android.asadullin.lesson1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.kpfu.itis.android.asadullin.lesson1.databinding.FragmentThirdBinding

class ThirdFragment : Fragment() {
    private var _viewBinding: FragmentThirdBinding? = null
    private val viewBinding: FragmentThirdBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentThirdBinding.inflate(inflater, container, false)
        val view = viewBinding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        val text = arguments?.getString(ParamsKey.MESSAGE_TEXT_KEY) ?: ""

        with (viewBinding) {
            if (text.isNotEmpty()) {
                tvThird.text = text
            } else {
                tvThird.text = "Третий экран"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    companion object {
        const val THIRD_FRAGMENT_TAG = "THIRD_FRAGMENT_TAG"

        fun newInstance(text: String) = ThirdFragment().apply {
            arguments = Bundle().apply {
                putString(ParamsKey.MESSAGE_TEXT_KEY, text)
            }
        }
    }
}