package ir.shahramkhandagi.cookguide.model

import java.io.Serializable

data class RecipeCategory(
    val id: Int,
    val name: String,
    val image: String,
    val recipes: List<Recipe>
) : Serializable
