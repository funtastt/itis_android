package ru.kpfu.itis.android.asadullin.lesson1

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView

class MainActivity : AppCompatActivity() {
    private var k: Int = 0
    private var messages = arrayOf(
        "Hello there!",
        "General Kenobi",
        "Смешная нарезка детей"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)

        val projectName = application.applicationInfo.packageName.split(".").last()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.fragment_container,
                    FirstFragment.newInstance(projectName),
                    FirstFragment.FIRST_FRAGMENT_TAG,
                ).add(
                    R.id.fragment_fourth,
                    FourthFragment.newInstance(""),
                    FourthFragment.FOURTH_FRAGMENT_TAG,
                )
                .commit()
        }

        val fragmentContainerView: FragmentContainerView = findViewById(R.id.fragment_fourth)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentContainerView.visibility = View.VISIBLE

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_fourth, FourthFragment().apply {
                    arguments = Bundle().apply {
                        putString("msg1", messages[0])
                        putString("msg2", messages[1])
                        putString("msg3", messages[2])
                    }
                })
                commit()
            }
        } else {
            fragmentContainerView.visibility = View.GONE
        }
    }

    fun passData(msg: String) {
        messages[k % 3] = msg
        k++

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_fourth, FourthFragment().apply {
                arguments = Bundle().apply {
                    putString("msg1", messages[0])
                    putString("msg2", messages[1])
                    putString("msg3", messages[2])
                }
            })
            commit()
        }
    }
}