package ru.kpfu.itis.android.asadullin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.AnimatedVectorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.android.asadullin.lesson1.R
import ru.kpfu.itis.android.asadullin.lesson1.databinding.ActivityFragmentContainerBinding
import ru.kpfu.itis.android.asadullin.util.NotificationUtil


class MainActivity : AppCompatActivity() {
    private var _viewBinding: ActivityFragmentContainerBinding? = null
    private val viewBinding get() = _viewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityFragmentContainerBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        val navController =
            (supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment)
                .navController

        NavigationUI.setupWithNavController(viewBinding.bnvMain, navController, false)

        val action = intent.getIntExtra("action", NO_ACTION)
        onIntentAction(action)

        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationsPermissionWithRationale()
                NotificationUtil.allowedToShowNotifications = false
            } else {
                NotificationUtil.allowedToShowNotifications = true
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationUtil.createNotificationChannels(this)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val action = intent?.getIntExtra("action", NO_ACTION) ?: NO_ACTION
        onIntentAction(action)
    }

    private fun onIntentAction(action: Int) {
        when (action) {
            ACTION_SHOW_MESSAGE -> {
                Snackbar
                    .make(
                        viewBinding.root,
                        getString(R.string.hello_from_notification),
                        Snackbar.LENGTH_SHORT
                    )
                    .show()
            }

            ACTION_SHOW_SETTINGS -> {
                (supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment)
                    .navController
                    .apply {
                        if (currentDestination?.id != R.id.settingsFragment) {
                            popBackStack(R.id.homeFragment, false)
                            navigate(R.id.homeFragment_to_settingsFragment)
                        }
                    }
            }
        }
    }

    private fun requestNotificationsPermissionWithRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            )
        ) {
            remindPermissionSettings()
        } else {
            requestNotificationsPermission()
        }
    }

    private fun requestNotificationsPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                NotificationUtil.allowedToShowNotifications =
                    grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            }
        }
        if (!NotificationUtil.allowedToShowNotifications) remindPermissionSettings()
    }

    fun remindPermissionSettings() {
        AlertDialog.Builder(this)
            .setTitle(R.string.allow_notifications_through_settings)
            .setIcon(getDrawable(R.drawable.notification_icon))
            .setMessage(R.string.please_allow_sending_notifications)
            .setPositiveButton(R.string.go_to_settings) { _, _ ->
                openApplicationSettings()
            }.create().show()
    }

    private fun openApplicationSettings() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:$packageName")
        )
        startActivityForResult(appSettingsIntent, PERMISSION_REQUEST_CODE)
    }

    override fun onDestroy() {
        _viewBinding = null
        super.onDestroy()
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1

        const val NO_ACTION = 0
        const val ACTION_SHOW_MESSAGE = 1
        const val ACTION_SHOW_SETTINGS = 2
    }
}