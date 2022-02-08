package com.groliks.cookingrecipes.view.editrecipe

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.groliks.cookingrecipes.R
import com.groliks.cookingrecipes.appComponent
import com.groliks.cookingrecipes.databinding.FragmentEditRecipeBinding
import com.groliks.cookingrecipes.view.editrecipe.ingredientslist.IngredientsAdapter
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class EditRecipeFragment : Fragment() {
    private val args: EditRecipeFragmentArgs by navArgs()
    private var _binding: FragmentEditRecipeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: EditRecipeViewModelFactory.Factory
    private val viewModel: EditRecipeViewModel by viewModels { viewModelFactory.create(args.recipeId) }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditRecipeBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ingredients.layoutManager = LinearLayoutManager(requireContext())
        binding.recipeName.doOnRecipeUpdate(viewModel::updateRecipeName)
        binding.recipeDescription.doOnRecipeUpdate(viewModel::updateRecipeDescription)
        binding.recipeCategory.doOnRecipeUpdate(viewModel::updateRecipeCategory)
        binding.recipeInstruction.doOnRecipeUpdate(viewModel::updateRecipeInstruction)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.recipe.collect {
                it?.also { recipe ->
                    binding.recipeName.setText(recipe.info.name)
                    binding.recipeDescription.setText(recipe.info.description)
                    binding.recipeCategory.setText(recipe.info.category)
                    binding.recipeInstruction.setText(recipe.info.instruction)
                    val adapter = IngredientsAdapter(
                        recipe.ingredients,
                        viewModel::updateIngredientName,
                        viewModel::updateIngredientMeasure,
                        viewModel::addIngredient
                    )
                    binding.ingredients.adapter = adapter

                    viewModel.setRecipeEditable()
                }
            }
        }
    }

    private fun EditText.doOnRecipeUpdate(callback: (String) -> Unit) {
        doAfterTextChanged { text ->
            if (viewModel.isRecipeEditable) {
                callback(text.toString())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit_recipe, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_recipe -> {
                viewModel.saveRecipe()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}