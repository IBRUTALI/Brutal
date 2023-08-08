package ighorosipov.brutal.data.repository

import ighorosipov.brutal.data.data_source.CocktailDao
import ighorosipov.brutal.domain.model.Cocktail
import ighorosipov.brutal.domain.repository.CocktailRepository

class CocktailRepositoryImpl(
    private val dao: CocktailDao
) : CocktailRepository {

    override suspend fun getCocktails(): List<Cocktail> {
        return dao.getCocktails()
    }

    override suspend fun findCocktailById(id: Int): Cocktail {
        return dao.findCocktailById(id)
    }

    override suspend fun insertCocktail(cocktail: Cocktail) {
        dao.insertCocktail(cocktail)
    }

    override suspend fun deleteCocktail(cocktail: Cocktail) {
        dao.deleteCocktail(cocktail)
    }

}