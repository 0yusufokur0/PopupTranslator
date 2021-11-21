package com.resurrection.popuptranslator.ui.main.home

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.resurrection.popuptranslator.data.Language
import com.resurrection.popuptranslator.data.remote.TranslateAPI
import com.resurrection.popuptranslator.databinding.FragmentHomeBinding
import com.resurrection.popuptranslator.ui.base.BaseFragment


import io.hamed.floatinglayout.FloatingLayout

import io.hamed.floatinglayout.callback.FloatingListener

import android.content.Intent
import android.net.Uri

import android.os.Build
import android.provider.Settings

import androidx.annotation.RequiresApi
import com.resurrection.popuptranslator.*


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>
(R.layout.fragment_home, HomeViewModel::class.java) {
    private var langFrom = Language.AUTO_DETECT
    private var langTo = Language.TURKISH
    private lateinit var simpleFloatingWindow: SimpleFloatingWindow

    @RequiresApi(Build.VERSION_CODES.P)
    override fun init(savedInstanceState: Bundle?) {

            Intent(requireContext(),FloatingViewService::class.java).also {
                requireContext().startService(it)
            }


        binding.result.setOnClickListener {
            translate()

        }

        translate()
        binding.langFrom.setOnClickListener {
            val popup = PopupMenu(requireContext(), it)
            popup.menuInflater.inflate(R.menu.lang_menu, popup.menu)
            popup.setOnMenuItemClickListener { menuItem: MenuItem ->
                binding.langFrom.text = menuItem.title
                langFrom = getModelFieldValue(Language, menuItem.title.toString()) as String
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
                langTo = getModelFieldValue(Language, menuItem.title.toString()) as String
                translate()
                return@setOnMenuItemClickListener true
            }

            popup.show()
        }
    }

    private fun translate() {
        if (langTo == Language.AUTO_DETECT) langTo = Language.TURKISH

        TranslateAPI(
                langFrom, langTo, "Hi",
                object : TranslateAPI.TranslateListener {
                    override fun onSuccess(translatedText: String?) {
                        tryCatch { binding.result.text = translatedText.toString() }
                    }
                    override fun onFailure(ErrorText: String?) {
                        tryCatch { binding.result.text = ErrorText.toString() }
                    }
                })
    }





    fun test(){
        var floatingLayout: FloatingLayout? = null
        val floatingListener: FloatingListener = object : FloatingListener {
            override fun onCreateListener(view: View) {}
            override fun onCloseListener() {}
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(requireContext())){
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + requireActivity().packageName)
            )
            startActivityForResult(intent, 25)
        }
        else{
            floatingLayout = FloatingLayout(requireContext(), R.layout.activity_home)
            floatingLayout!!.setFloatingListener(floatingListener)
            floatingLayout!!.create()
        }
    }

    /*
        simpleFloatingWindow = SimpleFloatingWindow(requireContext())
*/


/*
        simpleFloatingWindow = SimpleFloatingWindow(requireContext())

            if (requireContext().canDrawOverlays) simpleFloatingWindow.show()
            else startManageDrawOverlaysPermission()*/


/*    private fun startManageDrawOverlaysPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${requireContext().packageName}")
            ).let {
                startActivityForResult(it, 5)
            }
        }
    }*/


/*    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_DRAW_OVERLAY_PERMISSION -> {
                if (requireContext().canDrawOverlays) {
                    *//*simpleFloatingWindow.show()*//*
                } else {

                }
            }
        }
    }*/


/*    private var floatingLayout: FloatingLayout? = null
    private val floatingListener: FloatingListener = object : FloatingListener {
        override fun onCreateListener(view: View) {
            //binding.result.setOnClickListener{ floatingLayout!!.destroy() }
        }

        override fun onCloseListener() {

        }
    }*/
}


