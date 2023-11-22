package ru.kpfu.itis.android.asadullin.util
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class AirplaneModeChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Util.isAirplaneModeOn = intent?.getBooleanExtra("state", false) ?: return
        AirplaneModeOnVariable.isBoo = Util.isAirplaneModeOn
    }
}