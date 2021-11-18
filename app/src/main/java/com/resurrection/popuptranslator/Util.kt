package com.resurrection.popuptranslator

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.widget.Toast
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
