package com.groliks.cookingrecipes.view.dialogs

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.groliks.cookingrecipes.R

class DeleteRecipeDialog : ActionConfirmationDialog() {
    private val navArgs: DeleteRecipeDialogArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text = resources.getString(R.string.delete_recipe_warning, navArgs.recipeName)
        setupText(text)
        setupYesButton(DIALOG_KEY, RESULT_KEY, navArgs.recipeId)
        setupNoButton()
    }

    companion object {
        const val DIALOG_KEY = "delete_recipe_dialog_key"
        const val RESULT_KEY = "result_key"
    }
}