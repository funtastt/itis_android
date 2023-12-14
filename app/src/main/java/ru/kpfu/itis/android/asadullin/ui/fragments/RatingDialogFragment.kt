package ru.kpfu.itis.android.asadullin.ui.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kpfu.itis.android.asadullin.data.db.entity.UserMovieInteractionEntity
import ru.kpfu.itis.android.asadullin.databinding.DialogFragmentRatingBinding
import ru.kpfu.itis.android.asadullin.di.ServiceLocator

class RatingDialogFragment(private var movieId: Int, private val listener: RatingDialogListener) : DialogFragment() {
    private var _binding: DialogFragmentRatingBinding? = null
    private val binding: DialogFragmentRatingBinding get() = _binding!!

    private val sharedPreferences by lazy {
        requireActivity().getSharedPreferences("ApplicationPreferences", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentRatingBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val interactionDao = ServiceLocator.getDatabaseInstance().interactionDao



        lifecycleScope.launch(Dispatchers.IO) {
            val prevRating =
                interactionDao.getInteractionModelById(movieId = movieId, userId = getUserId()) ?: 0

            withContext(Dispatchers.Main) {
                with(binding) {
                    ratingBar.rating = prevRating.toFloat()

                    ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                        if (rating < 1.0f) {
                            ratingBar.rating = 1.0f
                        }
                    }

                    btnCancel.setOnClickListener {
                        dismiss()
                    }

                    btnConfirm.setOnClickListener {
                        val newRating = ratingBar.rating.toInt()
                        lifecycleScope.launch (Dispatchers.IO) {
                            if (prevRating == 0) {
                                interactionDao.insertInteractionModel(UserMovieInteractionEntity(
                                    userId = getUserId(),
                                    movieId = movieId,
                                    rating = newRating
                                ))
                            } else {
                                interactionDao.updateMovieRating(
                                    userId = getUserId(),
                                    movieId = movieId,
                                    rating = newRating
                                )
                            }
                            withContext(Dispatchers.Main) {
                                listener.onRatingConfirmed(movieId)
                                dismiss()
                            }
                        }
                    }
                }
            }
        }

    }

    private fun getUserId() = sharedPreferences.getInt("userId", -1)

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    interface RatingDialogListener {
        fun onRatingConfirmed(movieId: Int)
    }
}
