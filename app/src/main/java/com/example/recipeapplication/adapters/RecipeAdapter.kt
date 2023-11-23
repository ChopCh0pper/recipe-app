package com.example.recipeapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapplication.R
import com.example.recipeapplication.databinding.RvItemBinding
import com.example.recipeapplication.models.Recipe

class RecipeAdapter(private val recipeList: List<Recipe>): RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = recipeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipeList[position])
    }
}

class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding = RvItemBinding.bind(view)
    fun bind(recipe: Recipe) = with(binding) {
        tvTitle.text = recipe.title
        tvRating.text = recipe.rating.toString()
        //!добавить картинку
    }
}