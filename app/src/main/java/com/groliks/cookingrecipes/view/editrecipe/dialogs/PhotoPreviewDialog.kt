package com.groliks.cookingrecipes.view.editrecipe.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import coil.load
import com.groliks.cookingrecipes.R
import com.groliks.cookingrecipes.databinding.DialogPhotoPreviewBinding

class PhotoPreviewDialog : DialogFragment(), DialogInterface.OnClickListener {
    private val navArgs: PhotoPreviewDialogArgs by navArgs()
    private var _binding: DialogPhotoPreviewBinding? = null
    private val binding get() = _binding!!
    private var isPositiveButtonClicked = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogPhotoPreviewBinding.inflate(LayoutInflater.from(requireContext()))

        binding.photoPreview.load(navArgs.uri)

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setPositiveButton(R.string.ok, this)
            .setNegativeButton(R.string.cancel, this)
            .create()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            setFragmentResult(PHOTO_PREVIEW_KEY, bundleOf(PHOTO_CONFIRM_KEY to PHOTO_CONFIRMED))
            isPositiveButtonClicked = true
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        if (!isPositiveButtonClicked) {
            setFragmentResult(PHOTO_PREVIEW_KEY, bundleOf(PHOTO_CONFIRM_KEY to PHOTO_REJECTED))
        }
        super.onDismiss(dialog)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val PHOTO_PREVIEW_KEY = "photo_preview_key"
        const val PHOTO_CONFIRM_KEY = "photo_confirm_key"
        const val PHOTO_CONFIRMED = "photo_confirmed"
        const val PHOTO_REJECTED = "photo_rejected"
    }
}