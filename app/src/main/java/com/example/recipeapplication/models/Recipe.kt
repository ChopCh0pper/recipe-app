package com.example.recipeapplication.models

import com.google.firebase.database.Exclude

data class Recipe(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val mainImage: String = "",
    val rating: Double = 0.0,
    @get:Exclude //исключение поля из десериализации
    val steps: MutableList<RecipeStep> = mutableListOf()
)

data class RecipeStep(
    val number: Int,
    val descriptor: String,
    val image: String,
)
