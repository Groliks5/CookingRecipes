package com.groliks.cookingrecipes.view.editrecipe.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.groliks.cookingrecipes.databinding.DialogExitWithoutSavingBinding
import com.groliks.cookingrecipes.view.util.hideBackground

class ExitWithoutSavingDialog : DialogFragment() {
    private var _binding: DialogExitWithoutSavingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogExitWithoutSavingBinding.inflate(inflater, container, false)

        dialog?.hideBackground()

        setupYesButton()
        setupNoButton()

        return binding.root
    }

    private fun setupYesButton() {
        binding.yesButton.setOnClickListener {
            setFragmentResult(EXIT_WITHOUT_SAVING_KEY, bundleOf(EXIT_RESULT_KEY to EXIT_CONFIRMED))
            dismiss()
        }
    }

    private fun setupNoButton() {
        binding.noButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val EXIT_WITHOUT_SAVING_KEY = "exit_without_saving_key"
        const val EXIT_RESULT_KEY = "exit_result_key"
        const val EXIT_CONFIRMED = "exit_confirmed"
    }
}