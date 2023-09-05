package com.example.recipeapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.recipeapplication.fragments.LogInFragment
import com.example.recipeapplication.utils.initFirebase
import com.example.recipeapplication.utils.initUser
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFirebase()
        initUser()
        val btNav = findViewById<BottomNavigationView>(R.id.btNavigation)
        navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(btNav, navController)
    }
}