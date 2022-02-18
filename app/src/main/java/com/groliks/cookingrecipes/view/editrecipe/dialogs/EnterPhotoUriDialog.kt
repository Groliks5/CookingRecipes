package com.groliks.cookingrecipes.view.editrecipe.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.groliks.cookingrecipes.databinding.DialogEnterPhotoUriBinding
import com.groliks.cookingrecipes.view.util.hideBackground

class EnterPhotoUriDialog : DialogFragment() {
    private var _binding: DialogEnterPhotoUriBinding? = null
    private val binding get() = _binding!!
    private var isOkButtonClicked = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogEnterPhotoUriBinding.inflate(LayoutInflater.from(requireContext()))

        dialog?.hideBackground()

        setupOkButton()
        setupCancelButton()

        return binding.root
    }

    private fun setupOkButton() {
        binding.okButton.setOnClickListener {
            val uri = binding.photoUri.text.toString()
            setFragmentResult(ENTER_URI_KEY, bundleOf(URI_KEY to uri))
            isOkButtonClicked = true
            dismiss()
        }
    }

    private fun setupCancelButton() {
        binding.cancelButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        if (!isOkButtonClicked) {
            setFragmentResult(ENTER_URI_KEY, bundleOf(URI_KEY to null))
        }
        super.onDismiss(dialog)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val ENTER_URI_KEY = "enter_uri_key"
        const val URI_KEY = "uri_key"
    }
}