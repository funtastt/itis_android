package ru.kpfu.itis.android.asadullin.util

object AirplaneModeOnVariable {
    var isBoo = false
        set(boo) {
            field = boo
            if (listener != null) listener!!.onChange()
        }
    var listener: ChangeListener? = null

    interface ChangeListener {
        fun onChange()
    }
}