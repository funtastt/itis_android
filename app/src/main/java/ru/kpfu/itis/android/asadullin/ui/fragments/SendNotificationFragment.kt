package ru.kpfu.itis.android.asadullin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import ru.kpfu.itis.android.asadullin.MainActivity
import ru.kpfu.itis.android.asadullin.lesson1.R
import ru.kpfu.itis.android.asadullin.lesson1.databinding.FragmentSendNotificationBinding
import ru.kpfu.itis.android.asadullin.util.AirplaneModeOnVariable
import ru.kpfu.itis.android.asadullin.util.Util


class SendNotificationFragment : Fragment(R.layout.fragment_send_notification) {
    private var _viewBinding: FragmentSendNotificationBinding? = null
    private val viewBinding: FragmentSendNotificationBinding get() = _viewBinding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentSendNotificationBinding.inflate(inflater)
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        with(viewBinding) {
            btnShowNotification.setOnClickListener {
                if (!Util.allowedToShowNotifications) {
                    (activity as MainActivity).remindPermissionSettings()
                } else {
                    val title = etNotificationTitle.text.toString()
                    val content = etNotificationContent.text.toString()

                    if (title.isEmpty() || content.isEmpty()) {
                        Snackbar.make(root, "Notification title and content can't be empty!", Snackbar.LENGTH_SHORT).show()
                    } else {
                        Util.sendNotification(
                            requireContext(),
                            title,
                            content
                        )
                    }

                }
            }
            btnShowNotification.isEnabled = context?.let { !Util.isAirplaneModeOn(it) } ?: false
            AirplaneModeOnVariable.listener = object : AirplaneModeOnVariable.ChangeListener {
                override fun onChange() {
                    val isAirplaneModeOn = Util.isAirplaneModeOn

                    btnShowNotification.isEnabled = !isAirplaneModeOn
                    (activity as MainActivity).showWarningMessage(!isAirplaneModeOn)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }
}