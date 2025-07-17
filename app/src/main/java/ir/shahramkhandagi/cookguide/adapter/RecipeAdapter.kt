package ir.shahramkhandagi.cookguide.adapter

import android.R.attr.name
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.shahramkhandagi.cookguide.R
import ir.shahramkhandagi.cookguide.databinding.ItemRecipeBinding
import ir.shahramkhandagi.cookguide.model.Recipe
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions


class RecipeAdapter(
    private var recipes: List<Recipe>,
    private val onItemClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.recipeName.text = recipe.name

            val context = binding.root.context
            val imageResId = context.resources.getIdentifier(recipe.image, "drawable", context.packageName)

            // Glide با بهینه‌سازی
            Glide.with(context)
                .load(imageResId.takeIf { it != 0 } ?: R.drawable.logo)
                .apply(
                    RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo)
                )
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

    override fun getItemCount(): Int = recipes.size

    fun updateList(newRecipes: List<Recipe>) {
        // بهتره از DiffUtil استفاده کنید برای عملکرد بهتر
        recipes = newRecipes
        notifyDataSetChanged()
    }
}
