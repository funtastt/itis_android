package ru.kpfu.itis.android.asadullin.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.android.asadullin.MainActivity
import ru.kpfu.itis.android.asadullin.lesson1.R
import ru.kpfu.itis.android.asadullin.lesson1.databinding.FragmentCoroutinesBinding
import ru.kpfu.itis.android.asadullin.util.NotificationUtil
import java.util.concurrent.CancellationException

class CoroutinesFragment : Fragment(R.layout.fragment_coroutines) {
    private var _viewBinding: FragmentCoroutinesBinding? = null
    private val viewBinding: FragmentCoroutinesBinding get() = _viewBinding!!

    private var job: Job? = null
    private var coroutinesDone = 0
    private var stopOnBackground = false
    private var maxCoroutines = 0

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
                startAllCoroutines(
                    sbCoroutinesCount.progress,
                    cbAsync.isChecked,
                    cbStopOnBg.isChecked,
                )
            }
        }
    }

    private fun startAllCoroutines(n: Int, async: Boolean, stopOnBackground: Boolean) {
        maxCoroutines = n
        this.stopOnBackground = stopOnBackground
        job = lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                repeat(n) {
                    if (async) {
                        launch { startSingleCoroutine(it + 1, n) }
                    } else {
                        startSingleCoroutine(it + 1, n)
                    }
                }
            }
        }.also {
            it.invokeOnCompletion { cause ->
                if (cause == null) {
                    context?.let { it1 ->
                        NotificationUtil.sendNotification(
                            it1,
                            getString(R.string.success),
                            getString(R.string.job_done)
                        )
                    }
                } else if (cause is CancellationException) {
                    Log.e(
                        javaClass.name,
                        String.format(getString(R.string.cancelled_coroutines), coroutinesDone)
                    )
                }
                job = null
            }
        }
    }

    private suspend fun startSingleCoroutine(index: Int, total: Int) {
        delay(2000)
        Log.e(javaClass.name, String.format(getString(R.string.finished_coroutines), index, total))
        coroutinesDone++
    }

    override fun onStop() {
        if (stopOnBackground) {
            job?.cancel(
                CancellationException(context?.getString(R.string.app_went_on_bg))
            )
        }
        super.onStop()
    }
    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }
}