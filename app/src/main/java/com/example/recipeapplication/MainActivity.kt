package com.example.recipeapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btNav = findViewById<BottomNavigationView>(R.id.btNavigation)
        val navController = findNavController(R.id.nav_host_fragment)

        NavigationUI.setupWithNavController(btNav, navController)
    }
}