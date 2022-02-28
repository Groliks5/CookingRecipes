package com.groliks.cookingrecipes.view.editrecipe

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import coil.Coil
import coil.load
import coil.request.ImageRequest
import com.groliks.cookingrecipes.R
import com.groliks.cookingrecipes.appComponent
import com.groliks.cookingrecipes.data.util.LoadingStatus
import com.groliks.cookingrecipes.databinding.FragmentEditRecipeBinding
import com.groliks.cookingrecipes.view.editrecipe.dialogs.ExitWithoutSavingDialog
import com.groliks.cookingrecipes.view.editrecipe.dialogs.PhotoChoosingDialog
import com.groliks.cookingrecipes.view.editrecipe.dialogs.SavingRecipeDialog
import com.groliks.cookingrecipes.view.editrecipe.ingredientslist.IngredientTouchHelper
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

    private var isRecipeInfoEditable = false

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

        setupExitWithoutSavingListener()
        setupPhotoChoosingListener()

        return binding.root
    }

    private fun setupPhotoChoosingListener() {
        setFragmentResultListener(PhotoChoosingDialog.PHOTO_CHOOSING_KEY) { _, bundle ->
            val photoUriResult = bundle.getString(PhotoChoosingDialog.PHOTO_URI_KEY)
            photoUriResult?.also { photoUri ->
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

    private fun setupExitWithoutSavingListener() {
        setFragmentResultListener(ExitWithoutSavingDialog.DIALOG_KEY) { _, bundle ->
            val result = bundle.getString(ExitWithoutSavingDialog.RESULT_KEY)
            if (result == ExitWithoutSavingDialog.RESULT_YES) {
                findNavController().popBackStack(R.id.editRecipeFragment, true)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupIngredientsRecyclerView()
        setupRecipeInfoView()
        setupSaveResultListener()
    }

    private fun setupSaveResultListener() {
        setFragmentResultListener(SavingRecipeDialog.RESULT_KEY) { _, bundle ->
            val result = bundle.getString(SavingRecipeDialog.SAVING_RESULT_KEY)
            if (result == SavingRecipeDialog.CANCEL_SAVING) {
                viewModel.cancelSaving()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.savingStatus.collect { savingStatus ->
                when (savingStatus) {
                    is LoadingStatus.None -> {}
                    is LoadingStatus.Loading -> {}
                    is LoadingStatus.Success, is LoadingStatus.Error -> {
                        findNavController().popBackStack(R.id.editRecipeFragment, false)
                        val messageId =
                            if (savingStatus is LoadingStatus.Success) R.string.recipe_saved else R.string.failed_to_save_recipe
                        val message = requireContext().resources.getString(messageId)
                        Toast.makeText(
                            requireContext(),
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.saveResultReceived()
                    }
                }
            }
        }
    }

    private fun setupRecipeInfoView() {
        binding.recipeName.doOnRecipeUpdate(viewModel::updateRecipeName)
        binding.recipeDescription.doOnRecipeUpdate(viewModel::updateRecipeDescription)
        binding.recipeCategory.doOnRecipeUpdate(viewModel::updateRecipeCategory)
        binding.recipeInstruction.doOnRecipeUpdate(viewModel::updateRecipeInstruction)
        binding.recipePhoto.setOnClickListener {
            val action =
                EditRecipeFragmentDirections.actionEditRecipeFragmentToPhotoChoosingDialog()
            findNavController().navigate(action)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.recipeInfo.collect {
                it?.also { recipeInfo ->
                    isRecipeInfoEditable = false
                    binding.recipeName.setText(recipeInfo.name)
                    binding.recipeDescription.setText(recipeInfo.description)
                    binding.recipeCategory.setText(recipeInfo.category)
                    binding.recipeInstruction.setText(recipeInfo.instruction)
                    if (recipeInfo.photoUri.isNotBlank()) {
                        binding.recipePhoto.load(recipeInfo.photoUri)
                    }
                    (requireActivity() as? AppCompatActivity)?.supportActionBar?.title =
                        recipeInfo.name

                    isRecipeInfoEditable = true
                }
            }
        }
    }

    private fun setupIngredientsRecyclerView() {
        binding.ingredients.layoutManager = LinearLayoutManager(requireContext())
        val adapter = IngredientsAdapter(
            viewModel::updateIngredientName,
            viewModel::updateIngredientMeasure,
            viewModel::addIngredient,
            viewModel::swapIngredients,
            viewModel::deleteIngredient,
            viewLifecycleOwner.lifecycleScope,
        )
        binding.ingredients.adapter = adapter
        val ingredientTouchHelperCallback = IngredientTouchHelper(adapter)
        val ingredientTouchHelper = ItemTouchHelper(ingredientTouchHelperCallback)
        ingredientTouchHelper.attachToRecyclerView(binding.ingredients)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.ingredients.collect {
                it?.also { ingredients ->
                    adapter.submitIngredients(ingredients)
                }
            }
        }
    }

    private fun EditText.doOnRecipeUpdate(callback: (String) -> Unit) {
        doAfterTextChanged { text ->
            if (isRecipeInfoEditable) {
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
                    val action =
                        EditRecipeFragmentDirections.actionEditRecipeFragmentToSavingRecipeDialog()
                    findNavController().navigate(action)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onBackButtonPressed() {
        if (viewModel.isRecipeUpdated) {
            val action =
                EditRecipeFragmentDirections.actionEditRecipeFragmentToExitWithoutSavingDialog()
            findNavController().navigate(action)
        } else {
            findNavController().popBackStack(R.id.editRecipeFragment, true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}