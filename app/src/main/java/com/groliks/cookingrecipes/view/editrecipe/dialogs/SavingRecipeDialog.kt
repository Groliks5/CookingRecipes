package com.groliks.cookingrecipes.view.editrecipe.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.groliks.cookingrecipes.R

class SavingRecipeDialog : DialogFragment(), DialogInterface.OnClickListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setView(R.layout.dialog_saving_recipe)
            .setNegativeButton(R.string.cancel, this)
            .create()
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        onDismiss(dialog)
    }

    override fun onDismiss(dialog: DialogInterface) {
        setFragmentResult(RESULT_KEY, bundleOf(SAVING_RESULT_KEY to CANCEL_SAVING))
        super.onDismiss(dialog)
    }

    companion object {
        const val RESULT_KEY = "saving"
        const val SAVING_RESULT_KEY = "result"
        const val CANCEL_SAVING = "cancel"
    }
}