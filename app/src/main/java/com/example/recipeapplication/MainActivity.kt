package com.example.recipeapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.recipeapplication.fragments.LogInFragment
import com.example.recipeapplication.utils.FOLDER_AVATAR_IMAGE
import com.example.recipeapplication.utils.REF_STORAGE_ROOT
import com.example.recipeapplication.utils.UIDCURRENT_UID
import com.example.recipeapplication.utils.initFirebase
import com.example.recipeapplication.utils.initUser
import com.google.android.material.bottomnavigation.BottomNavigationView
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == RESULT_OK && data != null) {
            val uri = CropImage.getActivityResult(data).uri
            val path = REF_STORAGE_ROOT.child(FOLDER_AVATAR_IMAGE)
                .child(UIDCURRENT_UID)
            path.putFile(uri)
        }
    }
}