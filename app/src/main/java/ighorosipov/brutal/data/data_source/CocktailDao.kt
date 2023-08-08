package ighorosipov.brutal.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ighorosipov.brutal.domain.model.Cocktail
import kotlinx.coroutines.flow.Flow

@Dao
interface CocktailDao {

    @Query("SELECT * FROM cocktail")
    suspend fun getCocktails(): List<Cocktail>

    @Query("SELECT * FROM cocktail WHERE id = :id")
    suspend fun findCocktailById(id: Int): Cocktail

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCocktail(cocktail: Cocktail)

    @Delete
    suspend fun deleteCocktail(cocktail: Cocktail)

}