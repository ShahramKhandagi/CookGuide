package ir.shahramkhandagi.cookguide.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adivery.sdk.AdiveryAdListener
import com.adivery.sdk.AdiveryNativeAdView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import ir.shahramkhandagi.cookguide.R
import ir.shahramkhandagi.cookguide.databinding.ItemRecipeCategoryBinding
import ir.shahramkhandagi.cookguide.model.RecipeCategory
import ir.shahramkhandagi.cookguide.utils.SharedPreferencesManager

class RecipeCategoryAdapter(
    private val categories: List<RecipeCategory>,
    private val onItemClick: (RecipeCategory) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var showAd: Boolean = true
    private var isAdLoaded: Boolean = false

    companion object {
        const val VIEW_TYPE_CATEGORY = 0
        const val VIEW_TYPE_AD = 1
        private const val AD_POSITION = 2
    }

    override fun getItemCount(): Int {
        return if (showAd && categories.size >= AD_POSITION) categories.size + 1 else categories.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (showAd && position == AD_POSITION) VIEW_TYPE_AD else VIEW_TYPE_CATEGORY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_AD) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_adivery_native_ad, parent, false)
            AdViewHolder(view)
        } else {
            val binding = ItemRecipeCategoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            CategoryViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AdViewHolder -> holder.bind()
            is CategoryViewHolder -> {
                val categoryIndex = if (showAd && position > AD_POSITION) position - 1 else position
                holder.bind(categories[categoryIndex])
            }
        }
    }

    fun setAdVisibility(visible: Boolean) {
        showAd = visible
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(private val binding: ItemRecipeCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: RecipeCategory) {
            binding.categoryName.text = category.name

            val context = binding.root.context
            val imageResId = context.resources.getIdentifier(
                category.image, "drawable", context.packageName
            )

            Glide.with(context)
                .load(imageResId.takeIf { it != 0 } ?: R.drawable.logo)
                .apply(
                    RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(binding.categoryImage)

            binding.root.setOnClickListener { onItemClick(category) }
        }
    }

    inner class AdViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val adView: AdiveryNativeAdView? = view.findViewById(R.id.native_ad_view)

        fun bind() {
            val prefsManager = SharedPreferencesManager(itemView.context)

            if (prefsManager.isPremiumUser()) {
                itemView.visibility = View.GONE
            } else {
                adView?.setListener(object : AdiveryAdListener() {
                    override fun onAdLoaded() {
                        isAdLoaded = true
                        adView.visibility = View.VISIBLE
                    }

                    override fun onError(reason: String) {
                        isAdLoaded = false
                        adView.visibility = View.GONE
                    }

                    override fun onAdShown() {}
                    override fun onAdClicked() {}
                })
                adView?.loadAd()
            }
        }
    }
}