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
import ir.shahramkhandagi.cookguide.utils.PermissionUtils
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
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        originalCategories = Utils.loadRecipeCategoriesFromJson(requireContext())
        displayedCategories = originalCategories.toMutableList()
        PermissionUtils(requireActivity()).checkNotificationPermission()

        setupRecyclerView()

        return binding.root
    }


    private fun setupRecyclerView() {
        adapter = RecipeCategoryAdapter(displayedCategories) { category ->
            val action = HomeFragmentDirections.actionHomeFragmentToRecipeListFragment(category)
            findNavController().navigate(action)
        }

        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    RecipeCategoryAdapter.VIEW_TYPE_AD -> 2 // تبلیغ کل عرض رو بگیره
                    else -> 1 // آیتم عادی نصف عرض
                }
            }
        }

        binding.categoryRecyclerView.apply {
            layoutManager = gridLayoutManager
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            isNestedScrollingEnabled = false
            adapter = this@HomeFragment.adapter
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.setSearchableFragment(this)
    }

    override fun onSearchQuery(query: String) {
        val filtered = if (query.isEmpty()) {
            originalCategories
        } else {
            originalCategories.filter { it.name.contains(query, ignoreCase = true) }
        }

        // فقط زمانی لیست رو به‌روز کن که تغییر واقعی اتفاق افتاده باشه
        if (filtered != displayedCategories) {
            displayedCategories.clear()
            displayedCategories.addAll(filtered)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}