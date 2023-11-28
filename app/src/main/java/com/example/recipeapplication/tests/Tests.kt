package com.example.recipeapplication.tests

import com.example.recipeapplication.models.Recipe

public fun testForAdapter(): List<Recipe> {
    val list: List<Recipe> = listOf(
        Recipe("recipeId1!!", "USER.id", "Борщ", "", "", 5.0),
        Recipe("recipeId2!!", "USER.id", "Драники", "", "", 5.0),
        Recipe("recipeId3!!", "USER.id", "Котлетосы", "", "", 5.0)
    )
    return list
}