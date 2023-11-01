package ru.kpfu.itis.android.asadullin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.kpfu.itis.android.asadullin.MainActivity
import ru.kpfu.itis.android.asadullin.adapters.KittensAdapter
import ru.kpfu.itis.android.asadullin.databinding.FragmentKittensBinding
import ru.kpfu.itis.android.asadullin.model.KittenModel
import ru.kpfu.itis.android.asadullin.util.KittenItemsRepository
import ru.kpfu.itis.android.asadullin.util.ParamsKey
import ru.kpfu.itis.android.asadullin.util.callback.SwipeToDeleteCallback


class KittensFragment : Fragment() {
    private var _viewBinding: FragmentKittensBinding? = null
    private var rv : RecyclerView? = null
    private val viewBinding: FragmentKittensBinding get() = _viewBinding!!
    private var kittensAdapter: KittensAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentKittensBinding.inflate(inflater)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val kittensCount = requireArguments().getInt(ParamsKey.KITTENS_COUNT)

        initRecyclerView(kittensCount)
        if (kittensCount <= 12) enableSwipeToDelete()

        postponeEnterTransition()
        view.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                view.viewTreeObserver.removeOnPreDrawListener(this)
                startPostponedEnterTransition()
                return true
            }
        })
    }

    private fun initRecyclerView(kittensCount : Int) {
        val columnCount = if (kittensCount <= 12) 1 else 2

        val kittensList = KittenItemsRepository.getKittens(kittensCount)
        kittensAdapter = KittensAdapter(
            glide = Glide.with(this),
            fragmentManager = parentFragmentManager,
            onKittenClicked = ::onKittenClicked,
            onBookmarkClicked = ::onBookmarkClicked,
            root = requireView(),
            activity = requireActivity()
        )

        with(viewBinding) {
            if (kittensCount == 0) {
                tvNoNewsToShow.visibility = View.VISIBLE
                return
            }

            val gridLayoutManager = GridLayoutManager(
                requireContext(),
                columnCount,
                GridLayoutManager.VERTICAL,
                false
            )

            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int) =
                    if (columnCount == 1 || kittensList[position] is KittenModel.KittenData) 1
                    else 2
            }


            rvKittens.layoutManager = gridLayoutManager
            rvKittens.adapter = kittensAdapter

            rv = rvKittens

            kittensAdapter?.setItems(kittensList)
        }
    }


    private fun onKittenClicked(kittenPressed: KittenModel.KittenData) {
        (requireActivity() as MainActivity).replaceFragment(
            SingleKittenFragment.newInstance(kittenPressed.kittenId, "kitten_${kittenPressed.kittenId}"),
            SingleKittenFragment.CURRENT_KITTEN_FRAGMENT_TAG,
            true
        )
    }

    private fun enableSwipeToDelete() {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                val position = viewHolder.adapterPosition

                kittensAdapter?.removeItem(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(rv)
    }
    private fun onBookmarkClicked(position: Int, kittenData: KittenModel.KittenData) {
        KittenItemsRepository.toggleBookmark(kittenData.kittenId)
        kittensAdapter?.updateItem(position, kittenData)
    }

    override fun onDestroyView() {
        kittensAdapter = null
        super.onDestroyView()
    }


    companion object {
        const val KITTENS_FRAGMENT_TAG = "KITTENS_FRAGMENT_TAG"
        fun newInstance(kittensCount: Int) = KittensFragment().apply {
            arguments = bundleOf(ParamsKey.KITTENS_COUNT to kittensCount)
        }
    }
}