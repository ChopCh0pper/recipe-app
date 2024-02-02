package com.example.recipeapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipeapplication.R
import com.example.recipeapplication.adapters.RecipeAdapter
import com.example.recipeapplication.databinding.FragmentMyRecipesBinding
import com.example.recipeapplication.models.Recipe
import com.example.recipeapplication.utils.USER
import com.example.recipeapplication.utils.initRecipe

class MyRecipesFragment : Fragment() {
    private lateinit var binding: FragmentMyRecipesBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields()
    }

    private fun initFields() = with(binding) {
        navController = Navigation.findNavController(requireView())
        ibtAddRecipe.setOnClickListener {
            navController.navigate(R.id.action_myRecipesFragment_to_addRecipe)
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