package com.resurrection.popuptranslator.ui.main.floatingview

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.doOnTextChanged
import com.resurrection.popuptranslator.R
import com.resurrection.popuptranslator.data.Language
import com.resurrection.popuptranslator.data.remote.TranslateAPI
import com.resurrection.popuptranslator.databinding.OverlayLayoutBinding
import com.resurrection.popuptranslator.tryCatch
import com.resurrection.popuptranslator.ui.base.BaseFloatingView
import kotlinx.coroutines.*


class FloatingView : BaseFloatingView<OverlayLayoutBinding,FloatingVM>
    (R.layout.overlay_layout,FloatingVM::class.java) {
    private var langFrom = Language.AUTO_DETECT
    private var langTo = Language.TURKISH
    private var translateText = ""
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()
        showView()

        binding.moveBtn.setOnTouchListener(moveableTouchListener())
        binding.closeBtn.setOnClickListener { hideView() }
        binding.translateEditText.doOnTextChanged { text, start, before, count ->
            text?.let {
                translateText = text.toString()
                translate()
            }
        }
        MainScope().launch {
            withContext(Dispatchers.Default) { }
            delay(400)
            // open leyboard and focus on edit text
            val  imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE, 0)
            binding.translateEditText.requestFocus()
        }

    }

    private fun translate() {
        if (langTo == Language.AUTO_DETECT) langTo = Language.TURKISH

        TranslateAPI(
            langFrom, langTo, translateText,
            object : TranslateAPI.TranslateListener {
                override fun onSuccess(translatedText: String?) {
                    tryCatch { binding.result.text = translatedText.toString() }
                }

                override fun onFailure(ErrorText: String?) {
                    tryCatch { binding.result.text = ErrorText.toString() }
                }
            })
    }
}
