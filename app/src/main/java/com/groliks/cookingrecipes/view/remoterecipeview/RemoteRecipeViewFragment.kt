package com.groliks.cookingrecipes.view.remoterecipeview

import android.content.Context
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.groliks.cookingrecipes.appComponent
import com.groliks.cookingrecipes.view.recipeview.RecipeViewFragment
import com.groliks.cookingrecipes.view.recipeview.RecipeViewModel
import javax.inject.Inject

class RemoteRecipeViewFragment : RecipeViewFragment() {
    private val navArgs: RemoteRecipeViewFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: RemoteRecipeViewModelFactory.Factory
    override val viewModel: RecipeViewModel by viewModels { viewModelFactory.create(navArgs.recipeId) }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}