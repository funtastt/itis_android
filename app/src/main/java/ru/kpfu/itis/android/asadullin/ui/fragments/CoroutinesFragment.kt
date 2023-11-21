package ru.kpfu.itis.android.asadullin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import ru.kpfu.itis.android.asadullin.MainActivity
import ru.kpfu.itis.android.asadullin.lesson1.R
import ru.kpfu.itis.android.asadullin.lesson1.databinding.FragmentCoroutinesBinding
class CoroutinesFragment : Fragment(R.layout.fragment_coroutines) {
    private var _viewBinding: FragmentCoroutinesBinding? = null
    private val viewBinding: FragmentCoroutinesBinding get() = _viewBinding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentCoroutinesBinding.inflate(inflater)
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        with(viewBinding) {
            sbCoroutinesCount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    tvCoroutinesCount.text = String.format(getString(R.string.coroutines_count_d), progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) { }

                override fun onStopTrackingTouch(seekBar: SeekBar?) { }
            })

            tvCoroutinesCount.text = String.format(getString(R.string.coroutines_count_d), sbCoroutinesCount.progress)

            btnExecute.setOnClickListener {
                val async = cbAsync.isChecked
                val stopOnBackground = cbStopOnBg.isChecked
                (requireActivity() as MainActivity).startAllCoroutines(
                    sbCoroutinesCount.progress,
                    async,
                    stopOnBackground,
                )
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }
}