package ru.kpfu.itis.android.asadullin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import ru.kpfu.itis.android.asadullin.MainActivity
import ru.kpfu.itis.android.asadullin.adapters.KittensAdapter
import ru.kpfu.itis.android.asadullin.databinding.FragmentKittensBinding
import ru.kpfu.itis.android.asadullin.model.KittenModel
import ru.kpfu.itis.android.asadullin.util.KittenFactsRepository
import ru.kpfu.itis.android.asadullin.util.ParamsKey

class KittensFragment : Fragment() {
    private var _viewBinding: FragmentKittensBinding? = null
    private val viewBinding: FragmentKittensBinding get() = _viewBinding!!
    private var kittensAdapter: KittensAdapter? = null

    private final val MAX_NEWS_FEED_COUNT = 45

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
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val kittensCount = requireArguments().getInt(ParamsKey.KITTENS_COUNT)
        val columnCount = if (kittensCount <= 12) 1 else 2

        val kittensList = KittenFactsRepository.getKittens(kittensCount)
        kittensAdapter = KittensAdapter(
            glide = Glide.with(this),
            fragmentManager = parentFragmentManager,
            onKittenClicked = ::onKittenClicked,
            onBookmarkClicked = ::onBookmarkClicked,
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


            rvNewsFeed.layoutManager = gridLayoutManager
            rvNewsFeed.adapter = kittensAdapter

            kittensAdapter?.setItems(kittensList)
        }
    }

    private fun onKittenClicked(newsPressed: KittenModel.KittenData) {
        (requireActivity() as MainActivity).replaceFragment(
            SingleKittenFragment.newInstance(newsPressed.kittenId),
            SingleKittenFragment.CURRENT_KITTEN_FRAGMENT_TAG,
            true
        )
    }

    private fun onBookmarkClicked(position: Int, kittenData: KittenModel.KittenData) {
        KittenFactsRepository.toggleBookmark(kittenData.kittenId)
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