package ru.kpfu.itis.android.asadullin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)
    }

    fun replaceFragment(fragment: Fragment, tag: String, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment, tag)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
}