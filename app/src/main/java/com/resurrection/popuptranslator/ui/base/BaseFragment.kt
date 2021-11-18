package com.resurrection.popuptranslator.ui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.system.exitProcess

abstract class BaseFragment<VDB : ViewDataBinding,VM : ViewModel>
    (@LayoutRes private val layoutRes: Int, private val viewModelClass: Class<VM>) : Fragment(),
    LifecycleObserver {

    protected val viewModel by lazy {
        ViewModelProvider(this).get(viewModelClass)
    }

    private var _binding: VDB? = null
    val binding get() = _binding!!

    abstract fun init(savedInstanceState: Bundle?)

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater,layoutRes, container, false)
        return _binding!!.root
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun startActivity(sClass: Class<*>, bundle: Bundle) {
        val intent = Intent(activity, sClass)
        intent.putExtras(bundle)
        startActivity(intent)
    }


    protected fun reStartApp(sClass: Class<*>) {
        val intent = Intent(activity?.applicationContext, sClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        activity?.finish()
        exitProcess(0)
    }


    protected fun hideKeyboard() {
        activity?.currentFocus?.let {
            val inputManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }


}