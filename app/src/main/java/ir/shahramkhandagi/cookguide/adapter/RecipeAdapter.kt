package ir.shahramkhandagi.cookguide.adapter

import android.R.attr.name
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.shahramkhandagi.cookguide.R
import ir.shahramkhandagi.cookguide.databinding.ItemRecipeBinding
import ir.shahramkhandagi.cookguide.model.Recipe
import com.bumptech.glide.Glide



class RecipeAdapter(
    private var recipes: List<Recipe>,
    private val onItemClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.recipeName.text = recipe.name

            val context = binding.root.context

            val imageResId = context.resources.getIdentifier(recipe.image, "drawable", context.packageName)

            Glide.with(context)
                .load(imageResId.takeIf { it != 0 } ?: R.drawable.logo)
                .into(binding.recipeImage)

            binding.root.setOnClickListener { onItemClick(recipe) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount() = recipes.size

    fun updateList(newRecipes: List<Recipe>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }
}
