package ir.shahramkhandagi.cookguide.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonArray
import ir.shahramkhandagi.cookguide.model.RecipeCategory

object Utils {
    fun loadRecipeCategoriesFromJson(context: Context): List<RecipeCategory> {
        return try {
            val jsonFile = context.assets.open("recipes.json").bufferedReader().use { it.readText() }
            Log.d("LoadRecipeCategories", "JSON file loaded: $jsonFile") // لاگ محتوای JSON

            // تجزیه جیسون اصلی به شیء JsonObject
            val jsonObject = Gson().fromJson(jsonFile, JsonObject::class.java)

            // گرفتن آرایه "categories" از شیء JsonObject
            val categoriesJsonArray = jsonObject.getAsJsonArray("categories")
            Log.d("LoadRecipeCategories", "Categories array: $categoriesJsonArray") // لاگ آرایه دسته‌ها

            // تبدیل هر آیتم در آرایه به مدل داده‌ای RecipeCategory
            categoriesJsonArray.map { categoryJson ->
                Gson().fromJson(categoryJson, RecipeCategory::class.java)
            }
        } catch (e: Exception) {
            Log.e("LoadRecipeCategories", "Error loading JSON: ${e.message}", e)
            emptyList() // برگرداندن یک لیست خالی در صورت بروز خطا
        }
    }
}

