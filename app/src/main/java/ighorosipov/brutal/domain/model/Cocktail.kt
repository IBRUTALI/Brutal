package ighorosipov.brutal.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Cocktail(
    @PrimaryKey val id: Int? = null,
    val image: String? = null,
    val title: String,
    val description: String? = "",
    val recipe: String? = "",
    val ingredients: String
) : Serializable
