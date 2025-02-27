package ir.shahramkhandagi.cookguide.model

import java.io.Serializable

data class Recipe(
    val id: Int,
    val name: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val image: String,
    val description: List<String>
) : Serializable