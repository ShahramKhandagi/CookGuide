package ir.shahramkhandagi.cookguide.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import ir.shahramkhandagi.cookguide.R
import ir.shahramkhandagi.cookguide.databinding.ItemRecipeCategoryBinding
import ir.shahramkhandagi.cookguide.model.RecipeCategory

class RecipeCategoryAdapter(
    private val categories: List<RecipeCategory>,
    private val onItemClick: (RecipeCategory) -> Unit
) : RecyclerView.Adapter<RecipeCategoryAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemRecipeCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: RecipeCategory) {
            binding.categoryName.text = category.name

            val context = binding.root.context
            val imageResId = context.resources.getIdentifier(category.image, "drawable", context.packageName)

            // Glide با کش بهینه و placeholder
            Glide.with(context)
                .load(imageResId.takeIf { it != 0 } ?: R.drawable.logo)
                .apply(
                    RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)  // کش کامل
                )
                .into(binding.categoryImage)

            binding.root.setOnClickListener { onItemClick(category) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipeCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size
}