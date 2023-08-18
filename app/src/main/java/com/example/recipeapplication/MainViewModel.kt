package com.example.recipeapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class MainViewModel: ViewModel() {
    val liveDataAuth = MutableLiveData<FirebaseAuth>()
}