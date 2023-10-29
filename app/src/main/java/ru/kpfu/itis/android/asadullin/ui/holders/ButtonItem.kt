package ru.kpfu.itis.android.asadullin.ui.holders

import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.android.asadullin.adapters.KittensAdapter
import ru.kpfu.itis.android.asadullin.databinding.ItemKittensBsdBtnBinding
import ru.kpfu.itis.android.asadullin.ui.fragments.BottomSheetDialogFragment

class ButtonItem(
    private val viewBinding: ItemKittensBsdBtnBinding,
    private val adapter : KittensAdapter,
    private val fragmentManager: FragmentManager,
) : RecyclerView.ViewHolder(viewBinding.root) {
    fun onBind() {
        with(viewBinding) {
            btnShowBottomSheetDialog.setOnClickListener {
                val bottomSheetFragment = BottomSheetDialogFragment(adapter)
                bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
            }
        }
    }
}