package com.example.recipeapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.recipeapplication.utils.initFirebase
import com.example.recipeapplication.utils.initUser
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.annotations.Nullable
import com.theartofdev.edmodo.cropper.CropImage


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