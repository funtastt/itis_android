package ru.kpfu.itis.android.asadullin.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import ru.kpfu.itis.android.asadullin.R
import ru.kpfu.itis.android.asadullin.databinding.FragmentMovieItemBinding
import ru.kpfu.itis.android.asadullin.databinding.FragmentRatingDialogBinding

class RatingDialogFragment : DialogFragment() {
    private var _binding: FragmentRatingDialogBinding? = null
    private val binding: FragmentRatingDialogBinding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRatingDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnCancel.setOnClickListener {
                dismiss()
            }

            btnConfirm.setOnClickListener {
                val rating = ratingBar.rating
                Toast.makeText(context, "Selected rating: $rating", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
    }
}
