package ighorosipov.brutal.domain.repository

import ighorosipov.brutal.domain.model.Cocktail

interface CocktailRepository {

    suspend fun getCocktails(): List<Cocktail>

    suspend fun findCocktailById(id: Int): Cocktail

    suspend fun insertCocktail(cocktail: Cocktail)

    suspend fun deleteCocktail(cocktail: Cocktail)

}