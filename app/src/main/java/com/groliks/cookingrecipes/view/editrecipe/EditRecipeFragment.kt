package com.groliks.cookingrecipes.view.editrecipe

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.activity.addCallback
import androidx.core.graphics.drawable.toBitmap
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.Coil
import coil.load
import coil.request.ImageRequest
import com.groliks.cookingrecipes.R
import com.groliks.cookingrecipes.appComponent
import com.groliks.cookingrecipes.databinding.FragmentEditRecipeBinding
import com.groliks.cookingrecipes.view.editrecipe.dialogs.ExitWithoutSavingDialog
import com.groliks.cookingrecipes.view.editrecipe.dialogs.PhotoChoosingDialog
import com.groliks.cookingrecipes.view.editrecipe.dialogs.SavingRecipeDialog
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
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            onBackButtonPressed()
        }
        setupFragmentResultListeners()

        return binding.root
    }

    private fun setupFragmentResultListeners() {
        setFragmentResultListener(SavingRecipeDialog.RESULT_KEY) { _, bundle ->
            val result = bundle.getString(SavingRecipeDialog.SAVING_RESULT_KEY)
            if (result == SavingRecipeDialog.CANCEL_SAVING) {
                viewModel.cancelSaving()
            }
        }

        setFragmentResultListener(ExitWithoutSavingDialog.RESULT_KEY) { _, bundle ->
            val result = bundle.getString(ExitWithoutSavingDialog.EXIT_RESULT_KEY)
            if (result == ExitWithoutSavingDialog.EXIT_CONFIRMATION) {
                findNavController().popBackStack(R.id.edit_recipe, true)
            }
        }

        setFragmentResultListener(PhotoChoosingDialog.PHOTO_CHOOSING_KEY) { _, bundle ->
            val photoUri = bundle.getString(PhotoChoosingDialog.PHOTO_URI_KEY)
            photoUri?.also { photoUri ->
                val imageLoader = Coil.imageLoader(requireContext())
                val imageRequest = ImageRequest.Builder(requireContext())
                    .data(photoUri)
                    .target { photo ->
                        viewModel.updateRecipePhoto(photo.toBitmap())
                        binding.recipePhoto.setImageDrawable(photo)
                    }
                    .build()
                imageLoader.enqueue(imageRequest)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ingredients.layoutManager = LinearLayoutManager(requireContext())
        binding.recipeName.doOnRecipeUpdate(viewModel::updateRecipeName)
        binding.recipeDescription.doOnRecipeUpdate(viewModel::updateRecipeDescription)
        binding.recipeCategory.doOnRecipeUpdate(viewModel::updateRecipeCategory)
        binding.recipeInstruction.doOnRecipeUpdate(viewModel::updateRecipeInstruction)
        binding.recipePhoto.setOnClickListener {
            val action = EditRecipeFragmentDirections.choosePhoto()
            findNavController().navigate(action)
        }

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
                    if (recipe.info.photoUri.isNotBlank()) {
                        binding.recipePhoto.load(recipe.info.photoUri)
                    }

                    viewModel.setRecipeEditable()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isSaveFinished.collect { isSaveFinished ->
                if (isSaveFinished) {
                    findNavController().popBackStack()
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
                if (viewModel.isRecipeUpdated) {
                    viewModel.saveRecipe()
                    val action = EditRecipeFragmentDirections.saveRecipe()
                    findNavController().navigate(action)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onBackButtonPressed() {
        if (viewModel.isRecipeUpdated) {
            val action = EditRecipeFragmentDirections.exitWithoutSaving()
            findNavController().navigate(action)
        } else {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}