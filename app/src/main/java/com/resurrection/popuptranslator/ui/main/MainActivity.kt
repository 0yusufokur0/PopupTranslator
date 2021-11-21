package com.resurrection.popuptranslator.ui.main

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController

import com.resurrection.popuptranslator.R
import com.resurrection.popuptranslator.databinding.ActivityHomeBinding

import com.resurrection.popuptranslator.ui.base.BaseActivity

class MainActivity : BaseActivity<ActivityHomeBinding, MainActivityVM>
    (R.layout.activity_home, MainActivityVM::class.java) {

    override fun init(savedInstanceState: Bundle?) {

        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        navController.navigate(R.id.navigation_home)
    }

}
