package ighorosipov.cocktailapp.data.mapper

import ighorosipov.cocktailapp.data.model.CocktailEntity
import ighorosipov.cocktailapp.domain.model.Cocktail

class CocktailMapper {

    fun mapToEntity(cocktail: Cocktail): CocktailEntity = with(cocktail) {
        CocktailEntity(
            id = id,
            name = name,
            image = image,
            description = description,
            recipe = recipe,
            ingredients = ingredients
        )
    }

    fun mapToDomain(cocktailEntity: CocktailEntity): Cocktail = with(cocktailEntity) {
        Cocktail(
            id = id,
            name = name,
            image = image,
            description = description,
            recipe = recipe,
            ingredients = ingredients
        )
    }

}