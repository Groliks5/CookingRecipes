package com.groliks.cookingrecipes.view.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.groliks.cookingrecipes.R
import com.groliks.cookingrecipes.dataStore
import com.groliks.cookingrecipes.databinding.FragmentSettingsBinding
import java.util.*

class SettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSettingsBinding.inflate(inflater, container, false)

        setupThemeSelection(binding)
        setupLanguageSelection(binding)

        return binding.root
    }

    private fun setupThemeSelection(binding: FragmentSettingsBinding) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.darkThemeSwitch.isChecked = true
        }

        binding.darkThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            val nightMode =
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                requireContext().dataStore.edit { settings ->
                    settings[themePreferencesKey] = nightMode
                }
                AppCompatDelegate.setDefaultNightMode(nightMode)
            }
        }
    }

    private fun setupLanguageSelection(binding: FragmentSettingsBinding) {
        val currentLanguage = Language.getLanguage(Locale.getDefault().language)
        val arrayAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.languages,
            R.layout.item_spinner_language_selector
        )
        val languagePosition = when (currentLanguage) {
            Language.ENGLISH -> 0
            Language.RUSSIAN -> 1
        }
        binding.languageSelector.adapter = arrayAdapter
        binding.languageSelector.setSelection(languagePosition)

        binding.languageSelector.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position != languagePosition) {
                        val selectedLanguage = when (position) {
                            0 -> Language.ENGLISH
                            1 -> Language.RUSSIAN
                            else -> currentLanguage
                        }
                        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                            requireContext().dataStore.edit { settings ->
                                settings[languagePreferencesKey] = selectedLanguage.language
                            }
                            requireActivity().recreate()
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private enum class Language(val language: String) {
        RUSSIAN("ru"), ENGLISH("en");

        companion object {
            fun getLanguage(language: String): Language {
                return when (language) {
                    "ru" -> RUSSIAN
                    "en" -> ENGLISH
                    else -> ENGLISH
                }
            }
        }
    }

    companion object {
        val languagePreferencesKey = stringPreferencesKey("language")
        val themePreferencesKey = intPreferencesKey("theme")
    }
}