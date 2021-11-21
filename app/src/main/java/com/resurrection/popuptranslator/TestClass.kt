package com.resurrection.popuptranslator

import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.resurrection.popuptranslator.databinding.OverlayLayoutBinding

class TestClass : FloatingViewService<OverlayLayoutBinding>(R.layout.overlay_layout) {


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()
        addMyView()

        binding?.moveBtn?.setOnTouchListener(moveableTouchListener(true))
        binding.closeBtn.setOnClickListener {
            stopSelf()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun init() {



    }


}
