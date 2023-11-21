package ru.kpfu.itis.android.asadullin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import ru.kpfu.itis.android.asadullin.lesson1.R
import ru.kpfu.itis.android.asadullin.lesson1.databinding.FragmentSettingsBinding
import ru.kpfu.itis.android.asadullin.util.NotificationUtil
import ru.kpfu.itis.android.asadullin.util.NotificationUtil.Visibility
import ru.kpfu.itis.android.asadullin.util.NotificationUtil.Importance


class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private var _viewBinding: FragmentSettingsBinding? = null
    private val viewBinding: FragmentSettingsBinding get() = _viewBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentSettingsBinding.inflate(inflater)
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        with(viewBinding) {
            ArrayAdapter(
                requireContext(), android.R.layout.simple_spinner_item, Importance.values()
            ).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerImportance.adapter = it
                spinnerImportance.setSelection(NotificationUtil.importance.ordinal)
                spinnerImportance.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>, view: View?, position: Int, id: Long
                        ) {
                            val selected = parent.getItemAtPosition(position) as Importance
                            if (selected != NotificationUtil.importance) {
                                NotificationUtil.importance = selected
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            val selected = Importance.High
                            if (selected != NotificationUtil.importance) {
                                NotificationUtil.importance = selected
                                parent.setSelection(selected.ordinal)
                            }
                        }
                    }
            }

            ArrayAdapter(
                requireContext(), android.R.layout.simple_spinner_item, Visibility.values()
            ).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerVisibility.adapter = it
                spinnerVisibility.setSelection(NotificationUtil.visibility.ordinal)
                spinnerVisibility.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>, view: View?, position: Int, id: Long
                        ) {
                            NotificationUtil.visibility =
                                parent.getItemAtPosition(position) as Visibility
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            NotificationUtil.visibility = Visibility.Public
                            parent.setSelection(NotificationUtil.visibility.ordinal)
                        }
                    }
            }

            cbAllowBigText.isChecked = NotificationUtil.isBigTextNotification
            cbShowButtons.isChecked = NotificationUtil.areButtonsShown

            cbAllowBigText.setOnCheckedChangeListener { _, isChecked ->
                NotificationUtil.isBigTextNotification = isChecked
            }

            cbShowButtons.setOnCheckedChangeListener { _, isChecked ->
                NotificationUtil.areButtonsShown = isChecked
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }
}