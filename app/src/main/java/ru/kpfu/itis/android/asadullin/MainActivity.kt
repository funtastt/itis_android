package ru.kpfu.itis.android.asadullin

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.ChangeTransform
import android.transition.Transition
import android.transition.TransitionSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.kpfu.itis.android.asadullin.ui.fragments.GreetingFragment
import ru.kpfu.itis.android.asadullin.ui.fragments.SingleKittenFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_container)

        if (savedInstanceState == null) {
            replaceFragment(
                GreetingFragment(),
                GreetingFragment.GREETING_FRAGMENT_TAG,
                true
            )
        }
    }

    fun replaceFragment(fragment: Fragment, tag: String, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment, tag)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    fun showDetail(view: View, id : Int) {
        val transitionName = view.transitionName
        val fragment = SingleKittenFragment.newInstance(id, transitionName)
        fragment.sharedElementEnterTransition = getTransition()
        fragment.sharedElementReturnTransition = getTransition()
        supportFragmentManager
            .beginTransaction()
            .addSharedElement(view, transitionName)
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun getTransition(): Transition {
        val set = TransitionSet()
        set.ordering = TransitionSet.ORDERING_TOGETHER
        set.addTransition(ChangeBounds())
        set.addTransition(ChangeImageTransform())
        set.addTransition(ChangeTransform())
        return set
    }
}