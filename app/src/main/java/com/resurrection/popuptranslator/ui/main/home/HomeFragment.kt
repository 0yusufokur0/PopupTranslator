package com.resurrection.popuptranslator.ui.main.home

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import com.resurrection.popuptranslator.R
import com.resurrection.popuptranslator.data.Language
import com.resurrection.popuptranslator.data.remote.TranslateAPI
import com.resurrection.popuptranslator.databinding.FragmentHomeBinding
import com.resurrection.popuptranslator.getModelFieldValue

import com.resurrection.popuptranslator.ui.base.BaseFragment

class HomeFragment :BaseFragment<FragmentHomeBinding,HomeViewModel>
    (R.layout.fragment_home,HomeViewModel::class.java) {
    private var langFrom = Language.AUTO_DETECT
    private var langTo = Language.TURKISH

    override fun init(savedInstanceState: Bundle?) {
        translate()
        binding.langFrom.setOnClickListener {
            val popup = PopupMenu(requireContext(), it)
            popup.menuInflater.inflate(R.menu.lang_menu, popup.menu)
            popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                binding.langFrom.text = menuItem.title
                langFrom = getModelFieldValue(Language,menuItem.title.toString()) as String
                translate()
                return@setOnMenuItemClickListener true
            }
            popup.show()
        }

        binding.langTo.setOnClickListener {
            val popup = PopupMenu(requireContext(), it)
            popup.menuInflater.inflate(R.menu.lang_menu, popup.menu)
            popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                binding.langTo.text = menuItem.title
                langTo = getModelFieldValue(Language,menuItem.title.toString()) as String
                translate()
                return@setOnMenuItemClickListener true
            }

            popup.show()
        }
    }

    private fun translate(){
        var translateAPI = TranslateAPI(
            langFrom, langTo, "Hi",
            object : TranslateAPI.TranslateListener {
                override fun onSuccess(translatedText: String?) {
                    Toast.makeText(requireContext(), translatedText, Toast.LENGTH_LONG).show()
                }

                override fun onFailure(ErrorText: String?) {
                    Toast.makeText(requireContext(), ErrorText, Toast.LENGTH_LONG).show()
                }
            })
    }
}


