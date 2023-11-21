package ru.kpfu.itis.android.asadullin.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import ru.kpfu.itis.android.asadullin.MainActivity
import ru.kpfu.itis.android.asadullin.lesson1.R

object NotificationUtil {
    var importance = Importance.High
    var visibility = Visibility.Public
    var isBigTextNotification = false
    var areButtonsShown = false

    private fun getNotificationChannelId(importance: Importance) = "channel_${importance.name}"
    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            for (importance in Importance.values()) {
                NotificationChannel(
                    getNotificationChannelId(importance),
                    importance.importance,
                    importance.id,
                ).also {
                    notificationManager.createNotificationChannel(it)
                }
            }
        }
    }

    fun sendNotification(context: Context, title: String, text: String) {
        val channelId = getNotificationChannelId(importance)
        val builder = NotificationCompat.Builder(
            context,
            channelId,
        )
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(title)
            .setContentText(text)
            .setVisibility(visibility.id)
            .setPublicVersion(
                NotificationCompat.Builder(
                    context,
                    channelId
                )
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle(title)
                    .build()
            )

        if (isBigTextNotification) {
            builder.setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(text)
            )
        }

        if (areButtonsShown) {
            val showMessageIntent = Intent(context, MainActivity::class.java)
            showMessageIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            showMessageIntent.putExtra("action", MainActivity.ACTION_SHOW_MESSAGE)
            val pShowMessageIntent = PendingIntent.getActivity(
                context,
                0,
                showMessageIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
            builder.addAction(
                NotificationCompat.Action(
                    null,
                    "Show message",
                    pShowMessageIntent
                )
            )

            val showSettingsIntent = Intent(context, MainActivity::class.java)
            showSettingsIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            showSettingsIntent.putExtra("action", MainActivity.ACTION_SHOW_SETTINGS)
            val pShowSettingsIntent = PendingIntent.getActivity(
                context,
                1,
                showSettingsIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
            builder.addAction(
                NotificationCompat.Action(
                    null,
                    "Show settings",
                    pShowSettingsIntent
                )
            )
        }

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pIntent = PendingIntent.getActivity(
            context,
            2,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        builder.setContentIntent(pIntent)

        val notificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, builder.build())
    }

    enum class Importance(val importance: String, val id: Int) {
        Medium("Medium", NotificationManager.IMPORTANCE_LOW),
        High("High", NotificationManager.IMPORTANCE_DEFAULT),
        Urgent("Urgent", NotificationManager.IMPORTANCE_HIGH);
    }

    enum class Visibility(val visibility: String, val id: Int) {
        Public("Public", Notification.VISIBILITY_PUBLIC),
        Secret("Secret", Notification.VISIBILITY_SECRET),
        Private("Private", Notification.VISIBILITY_PRIVATE);
    }
}