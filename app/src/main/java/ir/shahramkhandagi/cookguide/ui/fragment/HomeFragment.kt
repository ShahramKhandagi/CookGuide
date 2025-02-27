package ir.shahramkhandagi.cookguide.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.navigation.fragment.findNavController
import ir.shahramkhandagi.cookguide.utils.Searchable

import ir.shahramkhandagi.cookguide.adapter.RecipeCategoryAdapter
import ir.shahramkhandagi.cookguide.databinding.FragmentHomeBinding
import ir.shahramkhandagi.cookguide.model.RecipeCategory
import ir.shahramkhandagi.cookguide.ui.activity.MainActivity
import ir.shahramkhandagi.cookguide.utils.Utils


class HomeFragment : Fragment(), Searchable {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var originalCategories: List<RecipeCategory>
    private lateinit var displayedCategories: MutableList<RecipeCategory>
    private lateinit var adapter: RecipeCategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // بارگذاری لیست دسته‌بندی‌ها از فایل JSON
        originalCategories = Utils.loadRecipeCategoriesFromJson(requireContext())
        displayedCategories = originalCategories.toMutableList()

        // تنظیم RecyclerView و Adapter
        binding.categoryRecyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = RecipeCategoryAdapter(displayedCategories) { category ->
            val action = HomeFragmentDirections.actionHomeFragmentToRecipeListFragment(category)
            findNavController().navigate(action)
        }
        binding.categoryRecyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.setSearchableFragment(this)
    }

    override fun onSearchQuery(query: String) {
        // فیلتر کردن دسته‌بندی‌ها براساس جستجو
        displayedCategories.clear()
        if (query.isEmpty()) {
            displayedCategories.addAll(originalCategories)
        } else {
            val filteredCategories = originalCategories.filter { category ->
                category.name.contains(query, ignoreCase = true)
            }
            displayedCategories.addAll(filteredCategories)
        }
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



