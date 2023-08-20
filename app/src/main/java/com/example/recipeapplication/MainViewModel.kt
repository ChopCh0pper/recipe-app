package com.example.recipeapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainViewModel: ViewModel() {
    val user = MutableLiveData<FirebaseUser>()
    val btClickedProfileFragment = MutableLiveData<String>()
}