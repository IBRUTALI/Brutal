package ighorosipov.cocktailapp.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ighorosipov.cocktailapp.data.model.CocktailEntity

@Dao
interface CocktailDao {

    @Query("SELECT * FROM cocktail")
    suspend fun getCocktails(): List<CocktailEntity>

    @Query("SELECT * FROM cocktail WHERE cocktail_id = :id")
    suspend fun findCocktailById(id: Int): CocktailEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCocktail(cocktail: CocktailEntity)

    @Delete
    suspend fun deleteCocktail(cocktail: CocktailEntity)

}