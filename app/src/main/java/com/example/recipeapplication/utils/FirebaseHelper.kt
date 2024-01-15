package com.example.recipeapplication.utils

import android.util.Log
import com.example.recipeapplication.models.Recipe
import com.example.recipeapplication.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

lateinit var AUTH: FirebaseAuth
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var CURRENT_UID: String
lateinit var USER: User
lateinit var RECIPE: Recipe

const val NODE_USERS = "users"
const val CHILD_ID = "id"
const val CHILD_USERNAME = "username"
const val CHILD_USER_RECIPES = "recipes"
const val CHILD_AVATAR_URI = "avatarUri"

const val NODE_RECIPES = "recipes"
const val CHILD_RECIPE_USER = "user"
const val CHILD_RECIPE_TITLE = "title"
const val CHILD_RECIPE_DESCRIPTION = "description"
const val CHILD_RECIPE_RATING = "rating"
const val CHILD_RECIPE_IMAGE = "image"
const val CHILD_RECIPE_STEPS = "steps"
const val CHILD_STEP_NUMBER = "number"
const val CHILD_STEP_DESCRIPTION = "description"
const val CHILD_STEP_IMAGE = "image"

const val FOLDER_AVATAR_IMAGE = "avatar_image"

fun initFirebase() {
    AUTH = FirebaseAuth.getInstance()
    USER = User()
    CURRENT_UID = AUTH.currentUser?.uid.toString()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
}

fun initUser() {
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                USER = snapshot.getValue(User::class.java) ?: User()

                // Достаем список recipes
                val recipes = snapshot.child("recipes").children
                for (recipe in recipes) {
                    USER.recipes.add(recipe.getValue(String::class.java) ?: "")
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })
}

fun initRecipe(recipeId: String, onCompleteListener: (Recipe) -> Unit) {
    REF_DATABASE_ROOT.child(NODE_RECIPES).child(recipeId)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                RECIPE = snapshot.getValue(Recipe::class.java) ?: Recipe()
                onCompleteListener(RECIPE)
            }

            override fun onCancelled(error: DatabaseError) {
                // Обработка ошибок, если необходимо
            }
        })
}