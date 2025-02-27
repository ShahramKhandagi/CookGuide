package ir.shahramkhandagi.cookguide.ui.fragment

import android.R.attr.bottom
import android.R.attr.left
import android.R.attr.right
import android.R.attr.top
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import android.widget.LinearLayout.LayoutParams
import androidx.core.content.res.ResourcesCompat

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.chip.Chip
import ir.shahramkhandagi.cookguide.R
import ir.shahramkhandagi.cookguide.databinding.FragmentRecipeDetailBinding
import ir.shahramkhandagi.cookguide.model.Instruction
import ir.shahramkhandagi.cookguide.model.Recipe


class RecipeDetailFragment : Fragment() {

    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding get() = _binding!!
    var isImgCrop: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)

        val recipe = arguments?.getSerializable("recipe") as Recipe
        binding.recipeName.text = recipe.name

        val imageResId = context?.resources?.getIdentifier(recipe.image, "drawable", context!!.packageName)

        Glide.with(requireContext())
            .load(imageResId.takeIf { it != 0 } ?: R.drawable.logo)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(binding.recipeImage)


        for (ingredient in recipe.ingredients) {
            val chip = Chip(requireContext()).apply {
                text = ingredient
                isClickable =
                    false // Chip ها انتخابی نیستند، می‌توانید این را تغییر دهید در صورت نیاز
                isCheckable = false  // برای انتخابی شدن Chip‌ها
                 setChipBackgroundColorResource(R.color.primary_color) // رنگ پس‌زمینه Chip
                // setTextColor(resources.getColorStateList(R.color.chip_text)) // رنگ متن Chip
                // setCheckedIconResource(R.drawable.ic_check) // آیکون انتخابی
//                 checkedIconVisible = true // نمایش آیکون در صورت انتخاب

                textSize = 11f
            }

            val typeface = ResourcesCompat.getFont(requireContext(), R.font.vazir_medium)
            chip.typeface = typeface
            val params: LayoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
            params.setMargins(4, 4, 0, 0)

            chip.layoutParams = params

            binding.recipeIngredients.addView(chip)
        }

        val stepTextViews = listOf(
            binding.tvStepText1,
            binding.tvStepText2,
            binding.tvStepText3,
            binding.tvStepText4,
            binding.tvStepText5,
            binding.tvStepText6,
            binding.tvStepText7,
            binding.tvStepText8,
            binding.tvStepText9,
            binding.tvStepText10
        )
        val stepNumViews = listOf(
            binding.tvStepNumber1,
            binding.tvStepNumber2,
            binding.tvStepNumber3,
            binding.tvStepNumber4,
            binding.tvStepNumber5,
            binding.tvStepNumber6,
            binding.tvStepNumber7,
            binding.tvStepNumber8,
            binding.tvStepNumber9,
            binding.tvStepNumber10
        )

        for (i in stepTextViews.indices) {
            if (i < recipe.instructions.size && recipe.instructions[i] != null) {
                stepTextViews[i].text = recipe.instructions[i]
                stepTextViews[i].visibility = View.VISIBLE
                stepNumViews[i].visibility = View.VISIBLE
            } else {
                stepTextViews[i].visibility = View.GONE
                stepNumViews[i].visibility = View.GONE
            }
        }

        binding.recipeDescription.text = recipe.description.joinToString("\n")

        // دریافت URL تصویر از arguments
        val imageUrl = arguments?.getString("img")

        // بارگذاری تصویر با Glide
        imageUrl?.let {
            Glide.with(requireContext())
                .load(it)
                .into(binding.recipeImage)
        }

//        // تنظیمات کلیک روی تصویر
//        binding.zoomImage.setOnClickListener { view ->
//            if (!isImgCrop) {
//                // تغییر به حالت FIT_CENTER
//                binding.recipeImage.scaleType = ImageView.ScaleType.FIT_CENTER
//                binding.imageGradient.setImageAlpha(0)
//
//                Glide.with(requireContext())
//                    .load(ContextCompat.getDrawable(requireContext(), R.drawable.min)) // تغییر به استفاده از ContextCompat
//                    .into(binding.recipeImage)
//
//                isImgCrop = true
//            } else {
//                binding.recipeImage.scaleType = ImageView.ScaleType.CENTER_CROP
//                binding.imageGradient.setImageAlpha(255)
//
//                imageUrl?.let {
//                    Glide.with(requireContext())
//                        .load(it)
//                        .into(binding.recipeImage)
//                }
//                isImgCrop = false
//            }
//        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(recipe: Recipe, imageUrl: String): RecipeDetailFragment {
            val fragment = RecipeDetailFragment()
            val args = Bundle()
            args.putSerializable("recipe", recipe)
            args.putString("img", imageUrl) // ارسال URL تصویر
            fragment.arguments = args
            return fragment
        }
    }
}
