package ru.kpfu.itis.android.asadullin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.kpfu.itis.android.asadullin.R
import ru.kpfu.itis.android.asadullin.di.ServiceLocator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)

        initInstances()
    }

    private fun initInstances() {
        ServiceLocator.createDatabase(context = this)
    }
}