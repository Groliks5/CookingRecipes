package com.groliks.cookingrecipes.view.editrecipe.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.groliks.cookingrecipes.R
import com.groliks.cookingrecipes.databinding.DialogEnterPhotoUriBinding

class EnterPhotoUriDialog : DialogFragment(), DialogInterface.OnClickListener {
    private var _binding: DialogEnterPhotoUriBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogEnterPhotoUriBinding.inflate(LayoutInflater.from(requireContext()))

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setPositiveButton(R.string.ok, this)
            .setNegativeButton(R.string.cancel, this)
            .create()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            val uri = binding.photoUri.text.toString()
            setFragmentResult(ENTER_URI_KEY, bundleOf(URI_KEY to uri))
        } else {
            setFragmentResult(ENTER_URI_KEY, bundleOf(URI_KEY to null))
        }
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