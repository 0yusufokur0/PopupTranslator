package com.resurrection.popuptranslator

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar
import com.resurrection.popuptranslator.databinding.OverlayLayoutBinding
import java.lang.Exception

fun  getModelFieldValue(model:Any,fieldName:String):Any?{
    val clazz = model.javaClass
    val field = clazz.getDeclaredField(fieldName)
    field.isAccessible = true
    return field.get(model)
}

fun getModelFieldsValues(model:Any):Map<String,Any>{
    val clazz = model.javaClass
    val fields = clazz.declaredFields
    val map = mutableMapOf<String,Any>()
    for (field in fields){
        field.isAccessible = true
        //key             //value
        map[field.name] = field.get(model)
    }
    return map
}

fun tryCatch(func:()->Unit){
    try {
        func()
    }catch (e:Exception){
        println("Error Try Catch:\n "+e.printStackTrace())
    }
}

private var toast: Toast? = null

fun Context.showToast(message: CharSequence?) {
    message?.let {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT).apply { show() }
    }
}

val Context.canDrawOverlays: Boolean
    get() = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)


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

fun <T>dataBinder(context: Context, layoutId: Int): T {
    val vdb :T =  DataBindingUtil.bind(LayoutInflater.from(context).inflate(layoutId,null))!!



    return vdb
}
