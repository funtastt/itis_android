package ru.kpfu.itis.android.asadullin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.kpfu.itis.android.asadullin.R
import ru.kpfu.itis.android.asadullin.adapters.KittensAdapter
import ru.kpfu.itis.android.asadullin.util.KittenFactsRepository

class BottomSheetDialogFragment(var adapter: KittensAdapter) : BottomSheetDialogFragment() {
    private var btnAddNews: Button? = null
    private var etAddNewsCount: EditText? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_bottom_sheet_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAddNews = view.findViewById(R.id.btn_addNews)
        etAddNewsCount = view.findViewById(R.id.et_addNewsCount)

        btnAddNews?.setOnClickListener {
            val number = etAddNewsCount?.text.toString().toIntOrNull() ?: 0
            if (number in 1..5) {
                adapter.setItems(KittenFactsRepository.addRandomItems(number))
            }
            dismiss()
        }
    }
    companion object {
        private val BOTTOM_SHEET_DIALOG_FRAGMENT_TAG = "BOTTOM_SHEET_DIALOG_FRAGMENT_TAG"
    }
}
