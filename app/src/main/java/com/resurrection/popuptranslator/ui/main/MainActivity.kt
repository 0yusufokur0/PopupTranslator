package com.resurrection.popuptranslator.ui.main

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.resurrection.popuptranslator.R
import com.resurrection.popuptranslator.databinding.ActivityHomeBinding

import com.resurrection.popuptranslator.ui.base.BaseActivity

class MainActivity : BaseActivity<ActivityHomeBinding, MainActivityVM>
    (R.layout.activity_home, MainActivityVM::class.java) {

    override fun init(savedInstanceState: Bundle?) {

        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        navController.navigate(R.id.navigation_home)

    }

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

}
