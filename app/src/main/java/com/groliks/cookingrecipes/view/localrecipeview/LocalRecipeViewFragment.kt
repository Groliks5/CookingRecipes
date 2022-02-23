package com.groliks.cookingrecipes.view.localrecipeview

import android.content.Context
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.groliks.cookingrecipes.appComponent
import com.groliks.cookingrecipes.view.recipeview.RecipeViewFragment
import javax.inject.Inject

class LocalRecipeViewFragment : RecipeViewFragment() {
    private val navArgs: LocalRecipeViewFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: LocalRecipeViewModelFactory.Factory
    override val viewModel: LocalRecipeViewModel by viewModels { viewModelFactory.create(navArgs.recipeId) }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}