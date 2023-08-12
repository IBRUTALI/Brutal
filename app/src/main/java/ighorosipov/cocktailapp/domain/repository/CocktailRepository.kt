package ighorosipov.cocktailapp.domain.repository

import ighorosipov.cocktailapp.domain.model.Cocktail
import ighorosipov.cocktailapp.domain.utils.Result

interface CocktailRepository {

    suspend fun getCocktails(): Result<List<Cocktail>>

    suspend fun findCocktailById(id: Int): Result<Cocktail>

    suspend fun insertCocktail(cocktail: Cocktail)

    suspend fun deleteCocktail(cocktail: Cocktail)

}