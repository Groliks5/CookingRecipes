package com.groliks.cookingrecipes.view.editrecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.groliks.cookingrecipes.databinding.FragmentEditRecipeBinding

class EditRecipeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEditRecipeBinding.inflate(inflater, container, false)

        return binding.root
    }
}