package com.resurrection.popuptranslator.ui.main.home

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import com.resurrection.popuptranslator.R
import com.resurrection.popuptranslator.data.Language
import com.resurrection.popuptranslator.data.remote.TranslateAPI
import com.resurrection.popuptranslator.databinding.FragmentHomeBinding

import com.resurrection.popuptranslator.ui.base.BaseFragment

class HomeFragment :BaseFragment<FragmentHomeBinding,HomeViewModel>
    (R.layout.fragment_home,HomeViewModel::class.java) {
    override fun init(savedInstanceState: Bundle?) {

         var translateAPI = TranslateAPI(
            Language.AUTO_DETECT, Language.TURKISH, "Hi",
            object : TranslateAPI.TranslateListener {
                override fun onSuccess(translatedText: String?) {
                    Toast.makeText(requireContext(), translatedText, Toast.LENGTH_LONG).show()
                }

                override fun onFailure(ErrorText: String?) {
                    Toast.makeText(requireContext(), ErrorText, Toast.LENGTH_LONG).show()
                }
            })

        binding.langFrom.setOnClickListener {
            val popup = PopupMenu(requireContext(), it)
            popup.menuInflater.inflate(R.menu.lang_menu, popup.menu)
            popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                // Respond to menu item click.
                binding.langFrom.text = menuItem.title
                return@setOnMenuItemClickListener true
            }
            popup.setOnDismissListener {
                // Respond to popup being dismissed.
            }
            // Show the popup menu.
            popup.show()
        }

        binding.langTo.setOnClickListener {
            val popup = PopupMenu(requireContext(), it)
            popup.menuInflater.inflate(R.menu.lang_menu, popup.menu)
            popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                binding.langTo.text = menuItem.title
                return@setOnMenuItemClickListener true
            }
            popup.setOnDismissListener {
                // Respond to popup being dismissed.
            }
            // Show the popup menu.
            popup.show()
        }

    }
}


