package com.example.recipeapplication.tests

import android.util.Log
import com.example.recipeapplication.models.Recipe
import com.example.recipeapplication.utils.CHILD_USER_RECIPES
import com.example.recipeapplication.utils.CURRENT_UID
import com.example.recipeapplication.utils.NODE_RECIPES
import com.example.recipeapplication.utils.NODE_USERS
import com.example.recipeapplication.utils.REF_DATABASE_ROOT
import com.example.recipeapplication.utils.USER
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

fun testForAdapter(): List<Recipe> {
    val list: List<Recipe> = listOf(
        Recipe("recipeId1!!", "USER.id", "Борщ", "", "", 5.0),
        Recipe("recipeId2!!", "USER.id", "Драники", "", "", 5.0),
        Recipe("recipeId3!!", "USER.id", "Котлетосы", "", "", 5.0)
    )
    return list
}

fun testForDB() {
    // Добавление рецептов в узел recipes
    val k1 = REF_DATABASE_ROOT.child(NODE_RECIPES).push()

    // Получение ID добавленных рецептов
    val recipeId1 = k1.key

    val r1 = Recipe(recipeId1!!, USER.id, "Борщ", "", "", 5.0)

    // Запись рецептов по их ID
    k1.setValue(r1)

    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
        .child(CHILD_USER_RECIPES).push().setValue(recipeId1)
}