package com.resurrection.popuptranslator

import android.annotation.SuppressLint
import android.content.Intent
import android.os.IBinder
import com.resurrection.popuptranslator.databinding.OverlayLayoutBinding
import com.resurrection.popuptranslator.ui.base.BaseFloatingView

class TestClass : BaseFloatingView<OverlayLayoutBinding>(R.layout.overlay_layout) {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()
        showView()

        binding.moveBtn.setOnTouchListener(moveableTouchListener())
        binding.closeBtn.setOnClickListener {
            hideView()
        }
    }

}
