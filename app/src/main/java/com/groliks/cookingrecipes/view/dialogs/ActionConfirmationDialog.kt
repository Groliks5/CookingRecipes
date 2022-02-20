package com.groliks.cookingrecipes.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.groliks.cookingrecipes.databinding.DialogActionConfirmationBinding
import com.groliks.cookingrecipes.view.util.hideBackground

abstract class ActionConfirmationDialog : DialogFragment() {
    private var _binding: DialogActionConfirmationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogActionConfirmationBinding.inflate(inflater, container, false)

        dialog?.hideBackground()

        return binding.root
    }

    protected fun setupText(text: String) {
        binding.warningText.text = text
    }

    protected fun setupYesButton(dialogKey: String, resultKey: String, result: Any?) {
        binding.yesButton.setOnClickListener {
            setFragmentResult(dialogKey, bundleOf(resultKey to result))
            dismiss()
        }
    }

    protected fun setupNoButton() {
        binding.noButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}