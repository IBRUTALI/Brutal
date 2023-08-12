package ighorosipov.cocktailapp.data.repository

import ighorosipov.cocktailapp.data.data_source.CocktailDao
import ighorosipov.cocktailapp.data.mapper.CocktailMapper
import ighorosipov.cocktailapp.domain.model.Cocktail
import ighorosipov.cocktailapp.domain.repository.CocktailRepository
import ighorosipov.cocktailapp.domain.utils.Result
import javax.inject.Inject

class CocktailRepositoryImpl @Inject constructor(
    private val dao: CocktailDao
) : CocktailRepository {

    override suspend fun getCocktails(): Result<List<Cocktail>> {
        return try {
            Result.Success(
                data = dao.getCocktails().map { CocktailMapper().mapToDomain(it) }
            )
        }catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun findCocktailById(id: Int): Result<Cocktail> {
        return try {
            Result.Success(
                data = CocktailMapper().mapToDomain(dao.findCocktailById(id))
            )
        }catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "An unknown error occurred.")
        }
    }

    override suspend fun insertCocktail(cocktail: Cocktail) {
        dao.insertCocktail(CocktailMapper().mapToEntity(cocktail))
    }

    override suspend fun deleteCocktail(cocktail: Cocktail) {
        dao.deleteCocktail(CocktailMapper().mapToEntity(cocktail))
    }

}