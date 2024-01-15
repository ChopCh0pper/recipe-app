package com.example.recipeapplication.fragments

import android.os.Bundle
import kotlinx.coroutines.*
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapplication.adapters.RecipeAdapter
import com.example.recipeapplication.databinding.FragmentMyRecipesBinding
import com.example.recipeapplication.models.Recipe
import com.example.recipeapplication.tests.testForAdapter
import com.example.recipeapplication.tests.testForDB
import com.example.recipeapplication.utils.RECIPE
import com.example.recipeapplication.utils.USER
import com.example.recipeapplication.utils.initRecipe

class MyRecipesFragment : Fragment() {
    private lateinit var binding: FragmentMyRecipesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields()
    }

    private fun initFields() = with(binding) {
        ibtAddRecipe.setOnClickListener {
            testForDB()
        }
        rvRecipes.apply {
            layoutManager = GridLayoutManager(context, 2)
            getRecipes { recipes ->
                adapter = RecipeAdapter(recipes)
            }
        }
    }

    private fun getRecipes(onCompleteListener: (List<Recipe>) -> Unit) {
        val recipeIds = USER.recipes
        val recipes = mutableListOf<Recipe>()

        var count = 0 // Счетчик для отслеживания завершения всех запросов

        for (recipeId in recipeIds) {
            initRecipe(recipeId) { recipe ->
                recipes.add(recipe)
                count++

                // Если все запросы завершены, вызываем onCompleteListener
                if (count == recipeIds.size) {
                    onCompleteListener(recipes)
                }
            }
        }
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