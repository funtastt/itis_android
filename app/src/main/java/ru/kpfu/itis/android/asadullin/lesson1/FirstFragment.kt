package ru.kpfu.itis.android.asadullin.lesson1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.kpfu.itis.android.asadullin.lesson1.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {
    private var _viewBinding: FragmentFirstBinding? = null
    private val viewBinding: FragmentFirstBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentFirstBinding.inflate(inflater, container, false)
        val view = viewBinding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        with(viewBinding) {
            tvFirst.text = arguments?.getString(ParamsKey.MESSAGE_TEXT_KEY) ?: ""

            btnNext.setOnClickListener{
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, SecondFragment.newInstance(etInput.text.toString()), SecondFragment.SECOND_FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit()

                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, ThirdFragment.newInstance(etInput.text.toString()), ThirdFragment.THIRD_FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    companion object {
        const val FIRST_FRAGMENT_TAG = "FIRST_FRAGMENT_TAG"

        fun newInstance(text: String) = FirstFragment().apply {
            arguments = Bundle().apply {
                putString(ParamsKey.MESSAGE_TEXT_KEY, text)
            }
        }
    }
}