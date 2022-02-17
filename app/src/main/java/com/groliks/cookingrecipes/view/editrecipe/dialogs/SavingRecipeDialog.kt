package com.groliks.cookingrecipes.view.editrecipe.dialogs

import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.groliks.cookingrecipes.R
import com.groliks.cookingrecipes.databinding.DialogSavingRecipeBinding

class SavingRecipeDialog : DialogFragment() {
    private lateinit var savingAnimator: ValueAnimator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DialogSavingRecipeBinding.inflate(inflater, container, false)

        binding.cancelButton.setOnClickListener { dismiss() }

        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        setupSavingAnimation(binding)

        return binding.root
    }

    private fun setupSavingAnimation(binding: DialogSavingRecipeBinding) {
        val typeEvaluator =
            TypeEvaluator<String> { fraction, startValue, endValue ->
                when {
                    fraction < 0.25 -> startValue
                    fraction < 0.5 -> "$startValue."
                    fraction < 0.75 -> "$startValue.."
                    else -> endValue
                }
            }
        val startText = requireContext().getString(R.string.saving)
        val endText = "$startText..."
        savingAnimator =
            ValueAnimator.ofObject(typeEvaluator, startText, endText).apply {
                duration = 2000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.RESTART
                addUpdateListener { updatedAnimation ->
                    binding.savingRecipe.text = updatedAnimation.animatedValue as String
                }
            }
    }

    override fun onStart() {
        super.onStart()
        Log.e("f", "f")
        savingAnimator.start()
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