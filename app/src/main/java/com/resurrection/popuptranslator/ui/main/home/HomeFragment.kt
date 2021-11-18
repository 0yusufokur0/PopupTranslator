package com.resurrection.popuptranslator.ui.main.home

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import com.google.android.material.snackbar.Snackbar
import com.resurrection.popuptranslator.data.Language
import com.resurrection.popuptranslator.data.remote.TranslateAPI
import com.resurrection.popuptranslator.databinding.FragmentHomeBinding
import com.resurrection.popuptranslator.getModelFieldValue
import com.resurrection.popuptranslator.tryCatch
import com.resurrection.popuptranslator.ui.base.BaseFragment


import io.hamed.floatinglayout.FloatingLayout

import io.hamed.floatinglayout.callback.FloatingListener
import android.content.Context

import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.Intent
import android.net.Uri

import android.os.Build
import android.provider.Settings

import android.widget.Toast
import androidx.annotation.RequiresApi
import com.aminography.floatingwindowapp.SimpleFloatingWindow
import com.resurrection.popuptranslator.R
import com.resurrection.popuptranslator.canDrawOverlays


class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>
(R.layout.fragment_home, HomeViewModel::class.java) {
    private var langFrom = Language.AUTO_DETECT
    private var langTo = Language.TURKISH
    private lateinit var simpleFloatingWindow: SimpleFloatingWindow

    @RequiresApi(Build.VERSION_CODES.P)
    override fun init(savedInstanceState: Bundle?) {

        simpleFloatingWindow = SimpleFloatingWindow(requireContext(),requireActivity())


            if (requireContext().canDrawOverlays) {
                simpleFloatingWindow.show()
            } else {
                startManageDrawOverlaysPermission()
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
        if (langTo == Language.AUTO_DETECT){
            langTo = Language.TURKISH
        }
        var translateAPI = TranslateAPI(
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
    private fun setupSnackBar(view: View, text: String? = "") {
        text?.let {
            val snackbar = Snackbar.make(view, text,
                    Snackbar.LENGTH_LONG).setAction("Action", null)
            snackbar.setActionTextColor(Color.BLUE)
            val snackbarView = snackbar.view
            snackbarView.setBackgroundColor(Color.LTGRAY)
            val textView =
                    snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(Color.BLUE)
            textView.textSize = 28f
            snackbar.show()
        }

    }


    private var floatingLayout: FloatingLayout? = null
    private val floatingListener: FloatingListener = object : FloatingListener {
        override fun onCreateListener(view: View) {

            //binding.result.setOnClickListener{ floatingLayout!!.destroy() }
        }

        override fun onCloseListener() {

        }
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



    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_DRAW_OVERLAY_PERMISSION -> {
                if (requireContext().canDrawOverlays) {
                    simpleFloatingWindow.show()
                } else {

                }
            }
        }
    }

    private fun startManageDrawOverlaysPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${requireContext().packageName}")
            ).let {
                startActivityForResult(it, REQUEST_CODE_DRAW_OVERLAY_PERMISSION)
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_DRAW_OVERLAY_PERMISSION = 5
    }



}


