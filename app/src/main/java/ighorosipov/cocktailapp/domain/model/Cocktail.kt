package ighorosipov.cocktailapp.domain.model

import java.io.Serializable

data class Cocktail(
    val id: Int? = null,
    val name: String,
    val image: String? = null,
    val description: String? = "",
    val recipe: String? = "",
    val ingredients: List<String>
): Serializable