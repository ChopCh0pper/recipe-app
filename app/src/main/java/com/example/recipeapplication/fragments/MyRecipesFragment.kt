package com.example.recipeapplication.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.recipeapplication.R
import com.example.recipeapplication.adapters.RecipeAdapter
import com.example.recipeapplication.databinding.ActivityMainBinding
import com.example.recipeapplication.databinding.FragmentMyRecipesBinding
import com.example.recipeapplication.models.Recipe
import com.example.recipeapplication.utils.CHILD_USER_RECIPES
import com.example.recipeapplication.utils.CURRENT_UID
import com.example.recipeapplication.utils.NODE_RECIPES
import com.example.recipeapplication.utils.NODE_USERS
import com.example.recipeapplication.utils.RECIPE
import com.example.recipeapplication.utils.REF_DATABASE_ROOT
import com.example.recipeapplication.utils.USER
import com.example.recipeapplication.utils.initRecipe
import com.example.recipeapplication.utils.updateUserRecipesList

class MyRecipesFragment : Fragment() {
    private lateinit var binding: FragmentMyRecipesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun test() {
        // Добавление рецептов в узел recipes
        val k1 = REF_DATABASE_ROOT.child(NODE_RECIPES).push()
        val k2 = REF_DATABASE_ROOT.child(NODE_RECIPES).push()
        val k3 = REF_DATABASE_ROOT.child(NODE_RECIPES).push()

        // Получение ID добавленных рецептов
        val recipeId1 = k1.key
        val recipeId2 = k2.key
        val recipeId3 = k3.key

        val r1 = Recipe(recipeId1!!, USER.id, "Борщ", "", "", 5.0)
        val r2 = Recipe(recipeId2!!, USER.id, "Драники", "", "", 5.0)
        val r3 = Recipe(recipeId3!!, USER.id, "Котлетосы", "", "", 5.0)

        // Запись рецептов по их ID
        k1.setValue(recipeId1?.let { r1.copy(id = it) })
        k2.setValue(recipeId2?.let { r2.copy(id = it) })
        k3.setValue(recipeId3?.let { r3.copy(id = it) })

        // Добавление ID рецептов в список recipes у пользователя
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
            .child(CHILD_USER_RECIPES).push().setValue(recipeId1)
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
            .child(CHILD_USER_RECIPES).push().setValue(recipeId2)
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
            .child(CHILD_USER_RECIPES).push().setValue(recipeId3)

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        test()
        initFields()
    }

    private fun initFields() = with(binding) {
        ibtAddRecipe.setOnClickListener {  }
        updateUserRecipesList {
            getRecipes { recipes ->
                Log.d("recipes", recipes.toString())
                rvRecipes.adapter = RecipeAdapter(recipes)
            }
        }
    }

    private fun getRecipes(onComplete: (List<Recipe>) -> Unit) {
        val recipeIds = USER.recipes
        var recipes = mutableListOf<Recipe>()

        if (recipeIds.isEmpty()) {
            onComplete(recipes)
            return
        }

        recipeIds.forEach { recipeId ->
            initRecipe(recipeId) { recipe ->
                recipes.add(recipe)
            }
        }
        onComplete(recipes)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = MyRecipesFragment()
    }
}