package com.resurrection.popuptranslator.ui.base

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import android.view.View.OnTouchListener
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.resurrection.popuptranslator.ui.main.MainActivity

@SuppressLint("RtlHardcoded")
abstract class BaseFloatingView<T : ViewDataBinding,VM : ViewModel>
    (private val layoutId: Int,
     private val viewModelClass: Class<VM>
     ) : Service() {

    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f
    private var mWindowManager: WindowManager? = null
    private var params: WindowManager.LayoutParams? = null
    val context = this

    lateinit var binding: T

    protected val viewModel by lazy {
        ViewModelProvider(ViewModelStoreOwner { ViewModelStore() }).get(viewModelClass)
    }

    override fun onCreate() {
        super.onCreate()
        binding = DataBindingUtil.bind(LayoutInflater.from(this).inflate(layoutId, null))!!
        val LAYOUT_FLAG: Int =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE

        params = WindowManager.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            View.FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        params?.let { //Specify the view position
            params!!.gravity =
                Gravity.TOP or Gravity.LEFT //Initially view will be added to top-left corner
            params!!.x = 0
            params!!.y = 100
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    open fun moveableTouchListener(openAppWhenClicked: Boolean = false): OnTouchListener? {
        return OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> { //remember the initial position.
                    initialX = params!!.x
                    initialY = params!!.y

                    //get the touch location
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    return@OnTouchListener true
                }
                MotionEvent.ACTION_UP -> {
                    val Xdiff = (event.rawX - initialTouchX).toInt()
                    val Ydiff = (event.rawY - initialTouchY).toInt()
                    if (openAppWhenClicked) if (Xdiff < 10 && Ydiff < 10) openApp() //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking. //So that is click event.
                    return@OnTouchListener true
                }
                MotionEvent.ACTION_MOVE -> { //Calculate the X and Y coordinates of the view.
                    params!!.x = initialX + (event.rawX - initialTouchX).toInt()
                    params!!.y = initialY + (event.rawY - initialTouchY).toInt()

                    //Update the layout with new X & Y coordinate
                    mWindowManager!!.updateViewLayout(binding.root, params)
                    return@OnTouchListener true
                }
            }
            false
        }
    }

    open fun showView() {
        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        mWindowManager?.addView(binding.root, params)
    }

    open fun hideView() {
        mWindowManager?.removeView(binding.root)
    }

    open fun openApp() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("fromwhere", "ser")
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        mWindowManager?.removeView(binding.root)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}