package ighorosipov.cocktailapp.di

import dagger.Binds
import dagger.Module
import ighorosipov.cocktailapp.data.repository.CocktailRepositoryImpl
import ighorosipov.cocktailapp.domain.repository.CocktailRepository

@Module
interface RepositoryModule {

    @Binds
    fun bindCocktailRepository(cocktailRepository: CocktailRepositoryImpl): CocktailRepository

}