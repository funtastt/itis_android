package ru.kpfu.itis.android.asadullin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import ru.kpfu.itis.android.asadullin.databinding.FragmentItemSingleKittenBinding
import ru.kpfu.itis.android.asadullin.model.KittenModel
import ru.kpfu.itis.android.asadullin.util.KittenItemsRepository
import ru.kpfu.itis.android.asadullin.util.ParamsKey

class SingleKittenFragment : Fragment() {
    private var _viewBinding: FragmentItemSingleKittenBinding? = null
    private val viewBinding: FragmentItemSingleKittenBinding get() = _viewBinding!!
    private val options: RequestOptions = RequestOptions().fitCenter().diskCacheStrategy(
        DiskCacheStrategy.ALL)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentItemSingleKittenBinding.inflate(inflater)



        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getInt(ParamsKey.CURRENT_KITTEN_ID)
        val transitionName = arguments?.getString(ParamsKey.TRANSITION_NAME)

        val currentKitten = KittenItemsRepository.getAllKittens().single { (it as? KittenModel.KittenData)?.kittenId == id } as KittenModel.KittenData

        with(viewBinding) {
            tvNewsTitle.text = currentKitten.kittenFactTitle
            tvNewsContent.text = currentKitten.kittenFactContent
            Glide.with(this@SingleKittenFragment)
                .load(currentKitten.kittenImageURL)
                .apply(options)
                .into(ivKittenPicture)
            ivKittenPicture.transitionName = transitionName
        }
    }

    override fun onDestroy() {
        _viewBinding = null
        super.onDestroy()
    }

    companion object {
        const val CURRENT_KITTEN_FRAGMENT_TAG = "CURRENT_KITTEN_FRAGMENT_TAG"
        fun newInstance(kittenId: Int, transitionName: String?) = SingleKittenFragment().apply {
            arguments = bundleOf(ParamsKey.CURRENT_KITTEN_ID to kittenId, ParamsKey.TRANSITION_NAME to transitionName)
        }
    }
}