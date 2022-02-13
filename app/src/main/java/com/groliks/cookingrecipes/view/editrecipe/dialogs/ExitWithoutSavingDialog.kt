package com.groliks.cookingrecipes.view.editrecipe.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.groliks.cookingrecipes.R

class ExitWithoutSavingDialog : DialogFragment(), DialogInterface.OnClickListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(requireContext())
            .setView(R.layout.dialog_exit_without_saving)
            .setPositiveButton(R.string.yes, this)
            .setNegativeButton(R.string.no, this)
            .create()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            setFragmentResult(RESULT_KEY, bundleOf(EXIT_RESULT_KEY to EXIT_CONFIRMATION))
        }
    }

    companion object {
        const val RESULT_KEY = "exit_without_saving"
        const val EXIT_RESULT_KEY = "result"
        const val EXIT_CONFIRMATION = "yes"
    }
}