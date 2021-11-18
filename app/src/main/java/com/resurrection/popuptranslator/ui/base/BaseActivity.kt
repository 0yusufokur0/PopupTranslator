package com.resurrection.popuptranslator.ui.base

import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.resurrection.popuptranslator.App
import kotlin.system.exitProcess


abstract class BaseActivity<VDB : ViewDataBinding,VM : ViewModel>
    (@LayoutRes private val layoutRes: Int,
     private val viewModelClass: Class<VM>
) : AppCompatActivity() {

    protected val viewModel by lazy {
        ViewModelProvider(this).get(viewModelClass)
    }

    open lateinit var binding: VDB

    abstract fun init(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutRes)
        init(savedInstanceState)
    }
    protected fun startActivity(sClass: Class<*>, bundle: Bundle? = null) {
        val intent = Intent(this, sClass)
        bundle?.let { intent.putExtras(bundle) }
        startActivity(intent)
    }

    protected fun reStartApp(sClass: Class<*>) {
        val intent = Intent(applicationContext, sClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
        exitProcess(0)
    }

    fun getApp(): App {
        return application as App
    }



}