package ir.shahramkhandagi.cookguide.ui.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import ir.shahramkhandagi.cookguide.R
import ir.shahramkhandagi.cookguide.utils.ResourcesDialogFragment
import ir.shahramkhandagi.cookguide.utils.Searchable
import ir.shahramkhandagi.cookguide.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var searchableFragment: Searchable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
        binding.searchView.isFocusable = false;


        val settingsIcon = findViewById<ImageView>(R.id.settingsIcon)
        settingsIcon.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                binding.drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.END)
            }
        }

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_star -> {
                    openBazaarReview()
                    true
                }
                R.id.nav_source -> {
                    val dialog = ResourcesDialogFragment()
                    dialog.show(supportFragmentManager, "ResourcesDialog")
                    true
                }

                R.id.nav_suggest -> {
                    val intent = Intent(this, FeedbackActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_settings -> {
                    Toast.makeText(this, "بزودی...", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_profile -> {
                    Toast.makeText(this, "بزودی...", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }

        // تنظیم NavController و AppBarConfiguration
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    supportActionBar?.show()
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    binding.searchView.visibility = View.VISIBLE
                }

                R.id.recipeDetailFragment -> {
//                    supportActionBar?.hide()
//                    binding.searchView.visibility = View.GONE
                    supportActionBar?.show()
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    binding.searchView.visibility = View.GONE
//                    binding.searchView.visibility = View.VISIBLE
                }

                else -> {
                    supportActionBar?.show()
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    binding.searchView.visibility = View.VISIBLE
                }
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    searchableFragment?.onSearchQuery(it)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    searchableFragment?.onSearchQuery(it)
                }
                return true
            }
        })


//        setCustomFontForSearchView(binding.searchView)
    }

    private fun openBazaarReview() {
        val appPackageName = "ir.shahramkhandagi.cookguide" // پکیج نیم اپلیکیشن شما
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("bazaar://details?id=$appPackageName"))
            intent.setPackage("ir.shahramkhandagi.cookguide") // مشخص کردن که این Intent فقط باید توسط کافه‌بازار باز شود
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // اگر کافه‌بازار نصب نیست، مرورگر را باز کنید
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://cafebazaar.ir/app/$appPackageName"))
            startActivity(intent)
        }
    }

//    private fun setCustomFontForSearchView(searchView: SearchView) {
//        try {
//            // بارگذاری فونت از res/font
//            val customFont = ResourcesCompat.getFont(this, R.font.vazir)
//
//            // دستیابی به EditText داخلی SearchView
//            val searchEditText =
//                searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
//
//            // اعمال فونت به EditText
//            searchEditText?.typeface = customFont
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }


    // تنظیم فرگمنت قابل جستجو
    fun setSearchableFragment(fragment: Searchable) {
        searchableFragment = fragment
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}

