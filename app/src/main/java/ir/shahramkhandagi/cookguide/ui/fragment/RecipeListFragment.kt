package ir.shahramkhandagi.cookguide.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.adivery.sdk.AdiveryAdListener
import com.adivery.sdk.AdiveryBannerAdView
import ir.shahramkhandagi.cookguide.utils.Searchable
import ir.shahramkhandagi.cookguide.adapter.RecipeAdapter
import ir.shahramkhandagi.cookguide.databinding.FragmentRecipeListBinding
import ir.shahramkhandagi.cookguide.model.Recipe
import ir.shahramkhandagi.cookguide.model.RecipeCategory
import ir.shahramkhandagi.cookguide.ui.activity.MainActivity


class RecipeListFragment : Fragment(), Searchable {

    private var _binding: FragmentRecipeListBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var recipes: List<Recipe>  // برای ذخیره‌سازی لیست اصلی جهت فیلتر کردن


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bannerAd: AdiveryBannerAdView = view.findViewById(ir.shahramkhandagi.cookguide.R.id.banner_ad)

        bannerAd.setBannerAdListener(object : AdiveryAdListener() {
            override fun onAdLoaded() {
                // تبلیغ به‌طور خودکار نمایش داده می‌شود، هر کار دیگری لازم است اینجا انجام دهید.
//                Toast.makeText(, "", Toast.LENGTH_SHORT).show()
            }

            override fun onError(reason: String) {
                // خطا را چاپ کنید تا از دلیل آن مطلع شوید
            }

            override fun onAdClicked() {
                // کاربر روی بنر کلیک کرده
            }
        })

        bannerAd.loadAd()
        
        // دریافت دسته‌بندی و لیست دستور غذاها
        val category = arguments?.getSerializable("category") as RecipeCategory
        recipes = category.recipes  // لیست اصلی را نگه می‌داریم

        // تنظیم RecyclerView
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        recipeAdapter = RecipeAdapter(recipes) { recipe ->
            val action = RecipeListFragmentDirections.actionRecipeListFragmentToRecipeDetailFragment2(recipe)
            findNavController().navigate(action)
        }
        binding.recyclerView.adapter = recipeAdapter

        // این فرگمنت را به عنوان فرگمنت قابل جستجو به اکتیویتی معرفی می‌کنیم
        (activity as? MainActivity)?.setSearchableFragment(this)
    }

    override fun onSearchQuery(query: String) {
        // فیلتر کردن لیست بر اساس جستجو
        val filteredRecipes = recipes.filter { recipe ->
            recipe.name.contains(query, ignoreCase = true)
        }
        recipeAdapter.updateList(filteredRecipes)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


