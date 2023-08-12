package ighorosipov.cocktailapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ighorosipov.cocktailapp.data.data_source.CocktailDao
import ighorosipov.cocktailapp.data.data_source.CocktailDatabase
import javax.inject.Singleton

@Module
interface DataModule {

    companion object {

        @Singleton
        @Provides
        fun provideCocktailDatabase(context: Context): CocktailDatabase {
            return CocktailDatabase.getDB(context)
        }

        @Singleton
        @Provides
        fun provideCocktailDao(cocktailDatabase: CocktailDatabase): CocktailDao {
            return cocktailDatabase.cocktailDao
        }

    }

}