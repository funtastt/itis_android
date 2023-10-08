package ru.kpfu.itis.android.asadullin.lesson1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)

        val projectName = application.applicationInfo.packageName.split(".").last()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container,
                    FirstFragment.newInstance(projectName),
                    FirstFragment.FIRST_FRAGMENT_TAG,
                )
                .commit()
        }
    }
}