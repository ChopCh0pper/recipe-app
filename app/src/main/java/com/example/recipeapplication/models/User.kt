package com.example.recipeapplication.models

import com.google.firebase.database.Exclude

data class User(
    val id: String = "",
    var username: String = "",
    val avatarUri: String = "",
    @get:Exclude //исключение поля из десериализаци
    var recipes: MutableList<String> = mutableListOf()
)
