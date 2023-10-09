package ru.kpfu.itis.android.asadullin.lesson1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.kpfu.itis.android.asadullin.lesson1.databinding.FragmentFourthBinding

class FourthFragment : Fragment(R.layout.fragment_fourth) {
    private var _binding: FragmentFourthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFourthBinding.inflate(inflater, container, false)
        val root = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        with(binding) {
            val arguments = requireArguments()
            tv1.text = arguments.getString("msg1")
            tv2.text = arguments.getString("msg2")
            tv3.text = arguments.getString("msg3")
        }
    }

    companion object {
        const val FOURTH_FRAGMENT_TAG = "FOURTH_FRAGMENT_TAG"

        fun newInstance(text: String) = FourthFragment().apply {
            arguments = Bundle().apply {
                putString(ParamsKey.MESSAGE_TEXT_KEY, text)
            }
        }
    }
}