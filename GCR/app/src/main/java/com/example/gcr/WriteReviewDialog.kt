package com.example.gcr

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.gcr.ViewModel.GameViewModel
import com.example.gcr.databinding.PopupCreateReviewBinding
import com.google.gson.JsonObject

class WriteReviewDialog() : DialogFragment() {

    private var _binding: PopupCreateReviewBinding? = null

    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PopupCreateReviewBinding.inflate(inflater, container, false)

        binding.btnCancel.setOnClickListener{
            dismiss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rbRecommended = binding.rbRecommended
        val rbMixed = binding.rbMixed
        val rbNotRecommended = binding.rbNotRecommended
        val etComment = binding.etComment

        binding.btnPost.setOnClickListener{
            val review_score = when {
                rbRecommended.isChecked -> "recommended"
                rbMixed.isChecked -> "mixed"
                rbNotRecommended.isChecked -> "not_recommended"
                else -> ""
            }
            val comment = etComment.text.toString()

            val review = Bundle().apply{
                putString("review_score", review_score)
                putString("comment", comment)
            }

            Log.d("WriteReviewDialog", "ENVIANDO RESULTADO -> score=$review_score comment=$comment")

            setFragmentResult("review", review)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}