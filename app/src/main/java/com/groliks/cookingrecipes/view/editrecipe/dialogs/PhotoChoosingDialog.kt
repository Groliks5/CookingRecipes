package com.groliks.cookingrecipes.view.editrecipe.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.groliks.cookingrecipes.R
import com.groliks.cookingrecipes.databinding.DialogPhotoChoosingBinding

private const val MIMETYPE_IMAGES = "image/*"
private const val SAVE_PHOTO_URI_KEY = "photo_uri_key"

class PhotoChoosingDialog : DialogFragment(), DialogInterface.OnClickListener {
    private var _binding: DialogPhotoChoosingBinding? = null
    private val binding get() = _binding!!
    private val getImageUriFromGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri: Uri? ->
            if (imageUri != null) {
                openPhotoPreview(imageUri.toString())
            }
        }
    private var photoUri: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogPhotoChoosingBinding.inflate(LayoutInflater.from(requireContext()))

        photoUri = savedInstanceState?.getString(SAVE_PHOTO_URI_KEY)

        binding.pickFromGallery.setOnClickListener {
            getImageUriFromGallery.launch(MIMETYPE_IMAGES)
        }
        binding.enterUri.setOnClickListener {
            dialog?.hide()
            val action = PhotoChoosingDialogDirections.enterUri()
            findNavController().navigate(action)
        }

        setFragmentResultListener(PhotoPreviewDialog.PHOTO_PREVIEW_KEY) { _, bundle ->
            if (bundle.getString(PhotoPreviewDialog.PHOTO_CONFIRM_KEY) == PhotoPreviewDialog.PHOTO_CONFIRMED) {
                setFragmentResult(PHOTO_CHOOSING_KEY, bundleOf(PHOTO_URI_KEY to photoUri))
                dismiss()
            } else {
                photoUri = null
                dialog?.show()
            }
        }

        setFragmentResultListener(EnterPhotoUriDialog.ENTER_URI_KEY) { _, bundle ->
            findNavController().popBackStack(R.id.photoChoosingDialog, false)
            dialog?.show()
            openPhotoPreview(bundle.getString(EnterPhotoUriDialog.URI_KEY))
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setNegativeButton(R.string.cancel, this)
            .setCancelable(false)
            .create()
    }

    private fun openPhotoPreview(uri: String?) {
        if (uri == null) {
            Toast.makeText(requireContext(), R.string.uri_not_found, Toast.LENGTH_SHORT).show()
        } else {
            photoUri = uri
            val action = PhotoChoosingDialogDirections.previewPhoto(uri)
            findNavController().navigate(action)
            dialog?.hide()
        }
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        dismiss()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        photoUri?.also {
            outState.putString(SAVE_PHOTO_URI_KEY, it)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val PHOTO_CHOOSING_KEY = "photo_choosing_key"
        const val PHOTO_URI_KEY = "photo_uri_key"
    }
}