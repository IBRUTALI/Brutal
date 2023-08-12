package ighorosipov.cocktailapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ighorosipov.cocktailapp.data.mapper.StringListConverter

@TypeConverters(StringListConverter::class)
@Entity(tableName = "Cocktail")
data class CocktailEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cocktail_id") val id: Int? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "image_uri") val image: String? = null,
    @ColumnInfo(name = "description") val description: String? = "",
    @ColumnInfo(name = "recipe") val recipe: String? = "",
    @ColumnInfo(name = "ingredients") val ingredients: List<String>
)
