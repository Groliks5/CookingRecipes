package com.groliks.cookingrecipes.view.editrecipe.dialogs

import android.os.Bundle
import android.view.View
import com.groliks.cookingrecipes.R
import com.groliks.cookingrecipes.view.dialogs.ActionConfirmationDialog

class ExitWithoutSavingDialog : ActionConfirmationDialog() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text = resources.getString(R.string.exit_without_saving_warning)
        setupText(text)
        setupYesButton(DIALOG_KEY, RESULT_KEY, RESULT_YES)
        setupNoButton()
    }

    companion object {
        const val DIALOG_KEY = "exit_without_saving_key"
        const val RESULT_KEY = "result_key"
        const val RESULT_YES = "result_yes"
    }
}