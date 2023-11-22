package ru.kpfu.itis.android.asadullin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.kpfu.itis.android.asadullin.MainActivity
import ru.kpfu.itis.android.asadullin.lesson1.R
import ru.kpfu.itis.android.asadullin.lesson1.databinding.FragmentSendNotificationBinding
import ru.kpfu.itis.android.asadullin.util.NotificationUtil

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
                if (!NotificationUtil.allowedToShowNotifications) {
                    (activity as MainActivity).remindPermissionSettings()
                } else {
                    NotificationUtil.sendNotification(
                        requireContext(),
                        etNotificationTitle.text.toString(),
                        etNotificationContent.text.toString(),
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }
}